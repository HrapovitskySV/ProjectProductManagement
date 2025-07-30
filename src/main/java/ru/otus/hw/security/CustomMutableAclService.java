package ru.otus.hw.security;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.jdbc.JdbcMutableAclService;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import ru.otus.hw.models.AclPermission;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.util.Objects.isNull;


public class CustomMutableAclService extends JdbcMutableAclService {

    private AclCache aclCache;

    public CustomMutableAclService(DataSource dataSource, LookupStrategy lookupStrategy, AclCache aclCache) {
        super(dataSource, lookupStrategy, aclCache);
        super.setSidIdentityQuery("select currval(pg_get_serial_sequence('acl_sid', 'id'))");
        super.setClassIdentityQuery("select currval(pg_get_serial_sequence('acl_class', 'id'))");
        this.aclCache = aclCache;
    }

    public Long createOrRetrieveSidPrimaryKey(String sidName, boolean sidIsPrincipal, boolean allowCreate) {
        return super.createOrRetrieveSidPrimaryKey(sidName, sidIsPrincipal,allowCreate);
    }


    public void updateSidPrimaryKey(String oldSidName, String newSidName, boolean sidIsPrincipal) {
        int  count = this.jdbcOperations.update("update acl_sid set sid=? where principal=? and sid=?",
                newSidName, sidIsPrincipal, oldSidName);

        if (count != 1) {
            throw new NotFoundException("Unable to locate ACL to update");
        }
        this.aclCache.clearCache();
    }


    public void createPermissionOnNewObject(Object object) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Sid owner = new PrincipalSid(authentication);
        ObjectIdentity oid = new ObjectIdentityImpl(object);

        List<Permission> permissions = List.of(BasePermission.READ,BasePermission.WRITE,BasePermission.DELETE);

        final Sid admin = new GrantedAuthoritySid("ROLE_ADMIN");

        MutableAcl acl = super.createAcl(oid);
        for (Permission permission: permissions) {
            acl.insertAce(acl.getEntries().size(), permission, owner, true);
            acl.insertAce(acl.getEntries().size(), permission, admin, true);
        }
        super.updateAcl(acl);
    }


    public MutableAcl createAclBySid(ObjectIdentity objectIdentity, Sid sid) throws AlreadyExistsException {
        Assert.notNull(objectIdentity, "Object Identity required");

        // Check this object identity hasn't already been persisted
        if (super.retrieveObjectIdentityPrimaryKey(objectIdentity) != null) {
            throw new AlreadyExistsException("Object identity '" + objectIdentity + "' already exists");
        }


        // Create the acl_object_identity row
        createObjectIdentity(objectIdentity, sid);

        // Retrieve the ACL via superclass (ensures cache registration, proper retrieval
        // etc)
        Acl acl = readAclById(objectIdentity);
        Assert.isInstanceOf(MutableAcl.class, acl, "MutableAcl should be been returned");

        return (MutableAcl) acl;
    }

    public void createPermission(Object object, String userName, boolean sidIsPrincipal, List<Permission> permissions) {
        Sid sid;
        if (sidIsPrincipal) {
             sid = new PrincipalSid(userName);
        } else {
            sid = new GrantedAuthoritySid(userName);
        }

        ObjectIdentity oid = new ObjectIdentityImpl(object);
        final Long oidKey = super.retrieveObjectIdentityPrimaryKey(oid);
        MutableAcl mAcl;
        if (isNull(oidKey)) {
            //значит Acl объекта такого еще нет, а значит и Entries нет
            mAcl = createAclBySid(oid, sid);
        } else {
            Acl acl = readAclById(oid);
            Assert.isInstanceOf(MutableAcl.class, acl, "MutableAcl should be been returned");

            mAcl = (MutableAcl) acl;
        }

        List<AccessControlEntry> entries = mAcl.getEntries();

        boolean entryFound = false;

        for (Permission permission: permissions) {
            entryFound = false;
            for (var entry: entries) {
                if (permission.equals(entry.getPermission()) &&  (sid.equals(entry.getSid()))) {
                    entryFound = true;
                    break;
                }
            }
            if (!entryFound) {
                mAcl.insertAce(mAcl.getEntries().size(), permission, sid, true);
            }
        }
        super.updateAcl(mAcl);
    }




    public void removePermission(ObjectIdentity oid, String userName) {
        Sid sid = new PrincipalSid(userName);

        final Long oidKey = super.retrieveObjectIdentityPrimaryKey(oid);
        if (isNull(oidKey)) {
            //значить Acl объекта такого еще нет, а значит и Entries нет
          return;
        }

        Acl acl = readAclById(oid);
        Assert.isInstanceOf(MutableAcl.class, acl, "MutableAcl should be been returned");
        MutableAcl mAcl = (MutableAcl) acl;

        List<AccessControlEntry> entries = acl.getEntries();
        AccessControlEntry entry;
        for (int i = 0; i < entries.size(); ++i) {
            entry = entries.get(i);
            if (sid.equals(entry.getSid())) {
                mAcl.deleteAce(i);
            }
        }
        updateAcl(mAcl);
    }


    public List<AclPermission> getPermissionOnClassAndPrincipal(Long objectIdClass, Long sid) {
        return this.jdbcOperations.query("select  e.mask , io.object_id_identity" +
                "from acl_entry e " +
                "   inner join acl_object_identity oi " +
                "       on e.acl_object_identity = oi.id" +
                "where io.object_id_class=? and e.sid=?", new AclRowMapper(), new Object[] { objectIdClass, sid });
    }

    private static class AclRowMapper implements RowMapper<AclPermission> {
        @Override
        public AclPermission mapRow(ResultSet rs, int i) throws SQLException {
            return new AclPermission(rs.getLong("id"),
                                    rs.getLong("acl_object_identity"),
                                    rs.getInt("ace_order"),
                                    rs.getLong("sid"),
                                    rs.getInt("mask"),
                                    rs.getBoolean("granting"),
                                    rs.getBoolean("audit_success"),
                                    rs.getBoolean("audit_failure"));

        }
    }
}


/* запрос на посмотреть права
SELECT c.class, oi.object_id_identity, p.name, s.sid, s.id sidid, e.*

FROM public.acl_sid s
	left join public.acl_entry e
	   inner join acl_object_identity oi
		   inner join acl_class c
				on c.id = oi.object_id_class
	   		inner join products p
				on oi.object_id_identity = p.id::varchar(36)
		on e.acl_object_identity = oi.id
	on s.id = e.sid
ORDER BY e.id ASC
 */