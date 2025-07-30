package ru.otus.hw.models.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long id;

    @NotBlank(message = "Name is mandatory")
    private String username;

    private String inputPassword;

    @Email(message = "Email should be valid")
    private String email;

    private boolean informAboutTasks;

    private Set<Long> products;

    private Set<Long> roles;
}
