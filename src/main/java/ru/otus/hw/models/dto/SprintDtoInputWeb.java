package ru.otus.hw.models.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SprintDtoInputWeb {
    private long id;

    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @NotNull(message = "Product is mandatory")
    @Positive(message = "Product is mandatory")

    private long productId;

    private LocalDate beginDate;

    private LocalDate endDate;
}
