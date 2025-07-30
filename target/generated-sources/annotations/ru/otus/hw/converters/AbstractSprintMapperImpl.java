package ru.otus.hw.converters;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Sprint;
import ru.otus.hw.models.dto.SprintDto;
import ru.otus.hw.models.dto.SprintDtoInputWeb;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T12:10:15+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class AbstractSprintMapperImpl extends AbstractSprintMapper {

    @Override
    public SprintDto toDto(Sprint sprint) {
        if ( sprint == null ) {
            return null;
        }

        SprintDto sprintDto = new SprintDto();

        sprintDto.setId( sprint.getId() );
        sprintDto.setName( sprint.getName() );
        sprintDto.setProduct( sprint.getProduct() );
        sprintDto.setBeginDate( sprint.getBeginDate() );
        sprintDto.setEndDate( sprint.getEndDate() );

        return sprintDto;
    }

    @Override
    public Sprint toModel(SprintDto sprintDto) {
        if ( sprintDto == null ) {
            return null;
        }

        Sprint sprint = new Sprint();

        sprint.setId( sprintDto.getId() );
        sprint.setName( sprintDto.getName() );
        sprint.setProduct( sprintDto.getProduct() );
        sprint.setBeginDate( sprintDto.getBeginDate() );
        sprint.setEndDate( sprintDto.getEndDate() );

        return sprint;
    }

    @Override
    public Sprint toModel(SprintDtoInputWeb sprintDto) {
        if ( sprintDto == null ) {
            return null;
        }

        Sprint sprint = new Sprint();

        sprint.setProduct( productIdToProduct( sprintDto.getProductId() ) );
        sprint.setId( sprintDto.getId() );
        sprint.setName( sprintDto.getName() );
        sprint.setBeginDate( sprintDto.getBeginDate() );
        sprint.setEndDate( sprintDto.getEndDate() );

        return sprint;
    }
}
