package ru.otus.hw.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.converters.RoleMapper;
import ru.otus.hw.models.Role;
import ru.otus.hw.repositories.RoleRepository;
import ru.otus.hw.security.CustomMutableAclService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final CustomMutableAclService customMutableAclService;

    private final RoleMapper roleMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<Role> findById(long id) {
        return roleRepository.findById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<Role> findAll() {
        return roleRepository.findAll();
    }


    @Override
    @Transactional()
    @PreAuthorize("hasRole('ADMIN')")
    public Role insert(String name) {
        var savedObj = save(new Role(0, name));

        // создаю новый Sid в Acl правах
        customMutableAclService.createOrRetrieveSidPrimaryKey("ROLE_" + name, false, true);

        return savedObj; //roleMapper.toDto(savedObj);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasRole('ADMIN')")
    public Role update(@Param("id")long id, String name) {
        var oRole = roleRepository.findById(id);
        if (oRole.isEmpty()) {
            throw new EntityNotFoundException("Role with id %s not found".formatted(id));
        }

        var role = oRole.get();

        // обновляю имя Sid в Acl правах
        customMutableAclService.updateSidPrimaryKey("ROLE_" + role.getName(), "ROLE_" + name, false);

        role.setName(name);
        var savedObj = save(role);
        return savedObj; //roleMapper.toDto(savedObj);
    }

    @Override
    @Transactional()
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@Param("id") long id) {
        roleRepository.deleteById(id);
    }



    private Role save(@Param("product") Role role) {
        return roleRepository.save(role);
    }



    public List<Role> findAllByIdWithsCheck(Set<Long> rolesIds) {
        var roles = roleRepository.findAllById(rolesIds);
        if (isEmpty(roles) || rolesIds.size() != roles.size()) {
            throw new EntityNotFoundException("One or all roles with ids %s not found".formatted(rolesIds));
        }

        return roles;
    }
}
