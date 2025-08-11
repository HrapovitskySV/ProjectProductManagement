package ru.otus.hw.converters;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.InfoSystem;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.dto.InfoSystemDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T12:10:15+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class AbstractInfoSystemMapperImpl extends AbstractInfoSystemMapper {

    @Override
    public InfoSystemDto toDto(InfoSystem infoSystem) {
        if ( infoSystem == null ) {
            return null;
        }

        InfoSystemDto infoSystemDto = new InfoSystemDto();

        infoSystemDto.setPrimaryProductId( infoSystemPrimaryProductId( infoSystem ) );
        infoSystemDto.setId( infoSystem.getId() );
        infoSystemDto.setName( infoSystem.getName() );
        infoSystemDto.setUseWebHook( infoSystem.getUseWebHook() );
        infoSystemDto.setUrlWebHook( infoSystem.getUrlWebHook() );

        return infoSystemDto;
    }

    @Override
    public InfoSystem toModel(InfoSystemDto infoSystemDto) {
        if ( infoSystemDto == null ) {
            return null;
        }

        InfoSystem infoSystem = new InfoSystem();

        infoSystem.setPrimaryProduct( productIdToProduct( infoSystemDto.getPrimaryProductId() ) );
        infoSystem.setId( infoSystemDto.getId() );
        infoSystem.setName( infoSystemDto.getName() );
        infoSystem.setUseWebHook( infoSystemDto.getUseWebHook() );
        infoSystem.setUrlWebHook( infoSystemDto.getUrlWebHook() );

        return infoSystem;
    }

    private long infoSystemPrimaryProductId(InfoSystem infoSystem) {
        if ( infoSystem == null ) {
            return 0L;
        }
        Product primaryProduct = infoSystem.getPrimaryProduct();
        if ( primaryProduct == null ) {
            return 0L;
        }
        long id = primaryProduct.getId();
        return id;
    }
}
