package ru.otus.hw.models;


public record AclPermission(long id,
                            long aclObjectIdentity,
                            int aceOrder,
                            long sid,
                            int mask,
                            boolean granting,
                            boolean auditSuccess,
                            boolean auditFailure) {

    public AclPermission(long id,
                            long aclObjectIdentity,
                            int aceOrder,
                            long sid,
                            int mask,
                            boolean granting,
                            boolean auditSuccess,
                            boolean auditFailure) {
        this.id = id;
        this.aclObjectIdentity = aclObjectIdentity;
        this.aceOrder = aceOrder;
        this.sid = sid;
        this.mask = mask;
        this.granting = granting;
        this.auditSuccess = auditSuccess;
        this.auditFailure = auditFailure;
    }


}

/*create table acl_entry(
	id bigserial primary key,
	acl_object_identity bigint not null,
	ace_order int not null,
	sid bigint not null,
	mask integer not null,
	granting boolean not null,
	audit_success boolean not null,
	audit_failure boolean not null,
	constraint unique_uk_4 unique(acl_object_identity,ace_order),
	constraint foreign_fk_4 foreign key(acl_object_identity) references acl_object_identity(id),
	constraint foreign_fk_5 foreign key(sid) references acl_sid(id)
);
*/
