package ru.otus.hw.converters;

import org.mapstruct.Mapper;
import ru.otus.hw.models.TaskType;
import ru.otus.hw.models.dto.TaskTypeDto;

@Mapper(componentModel = "spring")
public interface TaskTypeMapper {

    TaskTypeDto toDto(TaskType taskType);

    TaskType toModel(TaskTypeDto taskTypeDto);
}
