package ru.otus.hw.converters;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.InfoSystem;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.dto.InfoSystemDto;
import ru.otus.hw.services.ProductService;

@Mapper(componentModel = "spring")
public abstract class AbstractInfoSystemMapper {

    @Autowired
    private ProductService productService;

    @Mapping(target = "primaryProductId", source = "primaryProduct.id")
    public abstract InfoSystemDto toDto(InfoSystem infoSystem);


    @Mapping(target = "primaryProduct", source = "primaryProductId")
    public abstract InfoSystem toModel(InfoSystemDto infoSystemDto);

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
}
