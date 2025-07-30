package ru.otus.hw.converters;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.dto.UserDto;
import ru.otus.hw.models.dto.UserDtoFull;
import ru.otus.hw.models.dto.UserToListDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T12:10:15+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class AbstractUserConverterImpl extends AbstractUserConverter {

    @Override
    public UserToListDto toListDto(CustomUser user) {
        if ( user == null ) {
            return null;
        }

        long id = 0L;
        String username = null;
        String email = null;
        boolean informAboutTasks = false;

        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();
        informAboutTasks = user.isInformAboutTasks();

        UserToListDto userToListDto = new UserToListDto( id, username, email, informAboutTasks );

        return userToListDto;
    }

    @Override
    public UserDtoFull toDtoFull(CustomUser user) {
        if ( user == null ) {
            return null;
        }

        long id = 0L;
        String username = null;
        boolean informAboutTasks = false;
        String email = null;
        List<Role> roles = null;

        id = user.getId();
        username = user.getUsername();
        informAboutTasks = user.isInformAboutTasks();
        email = user.getEmail();
        List<Role> list = user.getRoles();
        if ( list != null ) {
            roles = new ArrayList<Role>( list );
        }

        String inputPassword = null;
        List<Product> products = null;

        UserDtoFull userDtoFull = new UserDtoFull( id, username, inputPassword, informAboutTasks, email, products, roles );

        return userDtoFull;
    }

    @Override
    public CustomUser toModel(UserDto userDto) {
        if ( userDto == null ) {
            return null;
        }

        CustomUser customUser = new CustomUser();

        fillUserRoles( userDto, customUser );

        customUser.setId( userDto.getId() );
        customUser.setUsername( userDto.getUsername() );
        customUser.setEmail( userDto.getEmail() );
        customUser.setInformAboutTasks( userDto.isInformAboutTasks() );

        return customUser;
    }
}
