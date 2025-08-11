package ru.otus.hw.converters;

import org.mapstruct.Mapper;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.dto.RoleDto;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleDto toDto(Role product);

    Role toModel(RoleDto poleDto);
}
