package ru.otus.hw.converters;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.TaskType;
import ru.otus.hw.models.dto.TaskTypeDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T12:10:15+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class TaskTypeMapperImpl implements TaskTypeMapper {

    @Override
    public TaskTypeDto toDto(TaskType taskType) {
        if ( taskType == null ) {
            return null;
        }

        long id = 0L;
        String name = null;

        id = taskType.getId();
        name = taskType.getName();

        TaskTypeDto taskTypeDto = new TaskTypeDto( id, name );

        return taskTypeDto;
    }

    @Override
    public TaskType toModel(TaskTypeDto taskTypeDto) {
        if ( taskTypeDto == null ) {
            return null;
        }

        TaskType taskType = new TaskType();

        taskType.setId( taskTypeDto.getId() );
        taskType.setName( taskTypeDto.getName() );

        return taskType;
    }
}
