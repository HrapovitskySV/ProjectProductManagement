package ru.otus.hw.services;

import ru.otus.hw.models.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleService {
    Optional<Role> findById(long id);

    List<Role> findAll();

    Role insert(String name);

    Role update(long id, String name);

    void deleteById(long id);

    List<Role> findAllByIdWithsCheck(Set<Long> rolesIds);
}
