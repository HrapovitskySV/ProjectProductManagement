package ru.otus.hw.converters;

import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.dto.UserDto;
import ru.otus.hw.models.dto.UserDtoFull;
import ru.otus.hw.models.dto.UserToListDto;
import ru.otus.hw.services.RoleService;


@Mapper(componentModel = "spring")
public abstract class AbstractUserConverter {

    @Autowired
    private RoleService roleService;


    public abstract UserToListDto toListDto(CustomUser user);

    @Mapping(target = "inputPassword", ignore = true)
    @Mapping(target = "products", ignore = true)
    public abstract UserDtoFull toDtoFull(CustomUser user);


    @BeforeMapping
    protected void fillUserRoles(UserDto userDto, @MappingTarget CustomUser user) {
        var roles = roleService.findAllByIdWithsCheck(userDto.getRoles());
        user.setRoles(roles);
    }


    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    public abstract CustomUser toModel(UserDto userDto);
}

