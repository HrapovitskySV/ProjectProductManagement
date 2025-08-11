package ru.otus.hw.models.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfoSystemDto {
    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private Boolean useWebHook;

    private String urlWebHook;

    private long primaryProductId;
}