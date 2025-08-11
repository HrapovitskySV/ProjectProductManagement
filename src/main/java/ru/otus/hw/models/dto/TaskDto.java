package ru.otus.hw.models.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.otus.hw.models.InfoSystem;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.TaskType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto implements Serializable {

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

    private Product product;

    private InfoSystem infoSystem;

    private UserToListDto author;

    //ответственный
    private UserToListDto responsible;

    private UserToListDto programmer;

    private UserToListDto analyst;

    private TaskType taskType;

    private String description;
}
