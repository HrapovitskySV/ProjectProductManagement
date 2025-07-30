package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskTypeDto {
    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;
}
