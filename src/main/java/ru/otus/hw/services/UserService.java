package ru.otus.hw.services;

import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.dto.UserDto;
import ru.otus.hw.models.dto.UserDtoFull;
import ru.otus.hw.models.dto.UserToListDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserDtoFull> findByIdDtoFull(long id);

    List<UserToListDto> findAlltoList();

    Optional<CustomUser> findById(long id);

    CustomUser insert(UserDto userDto);

    CustomUser update(UserDto userDto);

    void deleteById(long id);

    CustomUser save(CustomUser customUser);

    UserToListDto getCurrentUserDto();

    CustomUser getCurrentUser();

    Optional<CustomUser> findByUsername(String userName);
}
