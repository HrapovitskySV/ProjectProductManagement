package ru.otus.hw.converters;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.Sprint;
import ru.otus.hw.models.SprintComposition;
import ru.otus.hw.models.Task;
import ru.otus.hw.models.dto.ProductDto;
import ru.otus.hw.models.dto.SprintCompositionDto;
import ru.otus.hw.models.dto.SprintDto;
import ru.otus.hw.models.dto.TaskDto;

@Mapper(componentModel = "spring")
public abstract class AbstractSprintCompositionMapper {

    @Mapping(target = "taskId", source = "task.id")
    @Mapping(target = "taskName", source = "task.name")
    @Mapping(target = "taskTypeName", source = "task.taskType.name")
    @Mapping(target = "taskAuthorName", source = "task.author.username")
    @Mapping(target = "taskResponsibleName", source = "task.responsible.username")
    public abstract SprintCompositionDto toDto(SprintComposition sprintComposition);

    public TaskDto taskToTaskDto(Task task) {
        return Mappers.getMapper(AbstractTaskMapper.class).toDto(task);
    }

    public SprintDto sprintToSprintDto(Sprint sprint) {
        return Mappers.getMapper(AbstractSprintMapper.class).toDto(sprint);
    }

    public ProductDto productToProductDto(Product product) {
        return Mappers.getMapper(ProductMapper.class).toDto(product);
    }
}
