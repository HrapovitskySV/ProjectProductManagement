package ru.otus.hw.models.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.Product;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SprintDto {
    private long id;

    private String name;

    @NotBlank(message = "Product is mandatory")
    private Product product;

    private LocalDate beginDate;

    private LocalDate endDate;
}
