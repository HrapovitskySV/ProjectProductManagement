package ru.otus.hw.models;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;

import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Стратегия генерации идентификаторов
    private long id;

    @Column(name = "name")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @Column(name = "created")
    private LocalDateTime created;

    @Column(name = "laborcosts", precision = 10, scale = 2, nullable = false)//трудоемкость
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal laborcosts;

    @Column(name = "priority", precision = 10, scale = 2, nullable = false)//приортиет
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 10, fraction = 2)
    private BigDecimal priority;

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product product;

    @ManyToOne(targetEntity = InfoSystem.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "system_id", nullable = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private InfoSystem infoSystem;

    @ManyToOne(targetEntity = CustomUser.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CustomUser author;

    @ManyToOne(targetEntity = CustomUser.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "responsible_id", nullable = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    //ответственный
    private CustomUser responsible;

    @ManyToOne(targetEntity = CustomUser.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "programmer_id", nullable = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CustomUser programmer;

    @ManyToOne(targetEntity = CustomUser.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "analyst_id", nullable = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private CustomUser analyst;

    @ManyToOne(targetEntity = TaskType.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "tasktype_id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private TaskType taskType;


    @Column(name = "description")//
    private String description;
}
