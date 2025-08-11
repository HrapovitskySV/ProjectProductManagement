package ru.otus.hw.models;

import jakarta.validation.constraints.NotBlank;
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
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "infosystems")
@AllArgsConstructor
@NoArgsConstructor
public class InfoSystem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Стратегия генерации идентификаторов
    private long id;

    @Column(name = "name", unique = true, nullable = false, length = 255)
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    private String name;

    @Column(name = "use_web_hook")
    private Boolean useWebHook;

    @Column(name = "url_web_hook")
    @Size(min = 0, max = 255, message = "UrlWebHook must be between 0 and 255 characters")
    private String urlWebHook;

    @ManyToOne(targetEntity = Product.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_product_id", nullable = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Product primaryProduct;

}
