package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.hw.exceptions.UserNotFoundException;
import ru.otus.hw.models.dto.UserDto;
import ru.otus.hw.models.dto.UserDtoFull;
import ru.otus.hw.models.dto.UserToListDto;

import ru.otus.hw.services.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserRestController {


    private final UserService userService;


    @GetMapping("/api/users")
    public List<UserToListDto> listAllUsers() {

        List<UserToListDto> users = userService.findAlltoList();
        return users;
    }

    @GetMapping("/api/users/{id}")
    public UserDtoFull getUser(@PathVariable("id") long id) {
        return userService.findByIdDtoFull(id).orElseThrow(UserNotFoundException::new);
    }

    @PutMapping("/api/users")
    public ResponseEntity<String> saveUser(@Valid @RequestBody UserDto userDto) {
        userService.update(userDto);
        return ResponseEntity.ok("savedUser");
    }

    @PostMapping("/api/users")
    public ResponseEntity<String> insertUser(@Valid @RequestBody UserDto userDto) {
        userService.insert(userDto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body("ОК");
    }

    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) {
        userService.deleteById(id);
        return ResponseEntity.ok("deleted");
    }
}
