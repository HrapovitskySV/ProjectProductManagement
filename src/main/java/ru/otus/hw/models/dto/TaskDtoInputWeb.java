package ru.otus.hw.models.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDtoInputWeb {
    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    private LocalDateTime created;

    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal laborcosts;

    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal priority;

    @NotNull(message = "Product is mandatory")
    @Positive(message = "Product is mandatory")
    private long productId;

    private long infoSystemId;

    private long authorId;

    //ответственный
    private long responsibleId;

    private long programmerId;

    private long analystId;


    private long taskTypeId;

    private String description;
}
