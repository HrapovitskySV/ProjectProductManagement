package ru.otus.hw.models.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.Role;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDtoFull {
    private long id;

    private String username;

    private String inputPassword;

    private boolean informAboutTasks;

    @Email(message = "Email should be valid")
    private String email;

    private List<Product> products;

    private List<Role> roles;
}
