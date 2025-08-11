package ru.otus.hw.converters;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.Sprint;
import ru.otus.hw.models.dto.ProductDto;
import ru.otus.hw.models.dto.SprintDto;
import ru.otus.hw.models.dto.SprintDtoInputWeb;
import ru.otus.hw.services.ProductService;

@Mapper(componentModel = "spring")
public abstract class AbstractSprintMapper {

    @Autowired
    private ProductService productService;

    public abstract SprintDto toDto(Sprint sprint);

    public abstract Sprint toModel(SprintDto sprintDto);

    @Mapping(target = "product", source = "productId")
    public abstract Sprint toModel(SprintDtoInputWeb sprintDto);

    public Product productIdToProduct(long productId) {
        if (productId == 0) {
            return null;
        }
        var oProduct = productService.findById(productId);
        if (oProduct.isEmpty()) {
            throw new EntityNotFoundException("Product with id %s not found".formatted(productId));
        }
        return oProduct.get();
    }

    public ProductDto productToProductDto(Product product) {
        return Mappers.getMapper(ProductMapper.class).toDto(product);
    }
}
