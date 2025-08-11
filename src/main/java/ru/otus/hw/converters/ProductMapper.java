package ru.otus.hw.converters;

import org.mapstruct.Mapper;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.dto.ProductDto;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    Product toModel(ProductDto productDto);
}
