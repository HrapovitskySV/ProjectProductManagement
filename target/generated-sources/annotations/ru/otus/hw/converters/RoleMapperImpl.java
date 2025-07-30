package ru.otus.hw.converters;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.dto.RoleDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T12:10:15+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class RoleMapperImpl implements RoleMapper {

    @Override
    public RoleDto toDto(Role product) {
        if ( product == null ) {
            return null;
        }

        RoleDto roleDto = new RoleDto();

        roleDto.setId( product.getId() );
        roleDto.setName( product.getName() );

        return roleDto;
    }

    @Override
    public Role toModel(RoleDto poleDto) {
        if ( poleDto == null ) {
            return null;
        }

        Role role = new Role();

        role.setId( poleDto.getId() );
        role.setName( poleDto.getName() );

        return role;
    }
}
