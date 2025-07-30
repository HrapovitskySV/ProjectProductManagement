package ru.otus.hw.converters;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Task;
import ru.otus.hw.models.dto.TaskDto;
import ru.otus.hw.models.dto.TaskDtoInputWeb;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T12:00:24+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class AbstractTaskMapperImpl extends AbstractTaskMapper {

    @Override
    public TaskDto toDto(Task task) {
        if ( task == null ) {
            return null;
        }

        TaskDto taskDto = new TaskDto();

        taskDto.setId( task.getId() );
        taskDto.setName( task.getName() );
        taskDto.setCreated( task.getCreated() );
        taskDto.setLaborcosts( task.getLaborcosts() );
        taskDto.setPriority( task.getPriority() );
        taskDto.setProduct( task.getProduct() );
        taskDto.setInfoSystem( task.getInfoSystem() );
        taskDto.setAuthor( userToUserToListDto( task.getAuthor() ) );
        taskDto.setResponsible( userToUserToListDto( task.getResponsible() ) );
        taskDto.setProgrammer( userToUserToListDto( task.getProgrammer() ) );
        taskDto.setAnalyst( userToUserToListDto( task.getAnalyst() ) );
        taskDto.setTaskType( task.getTaskType() );
        taskDto.setDescription( task.getDescription() );

        return taskDto;
    }

    @Override
    public Task toModel(TaskDtoInputWeb taskDto) {
        if ( taskDto == null ) {
            return null;
        }

        Task task = new Task();

        task.setProduct( productIdToProduct( taskDto.getProductId() ) );
        task.setTaskType( taskTypeIdToTaskType( taskDto.getTaskTypeId() ) );
        task.setResponsible( userIdToUser( taskDto.getResponsibleId() ) );
        task.setAuthor( userIdToUser( taskDto.getAuthorId() ) );
        task.setProgrammer( userIdToUser( taskDto.getProgrammerId() ) );
        task.setAnalyst( userIdToUser( taskDto.getAnalystId() ) );
        task.setInfoSystem( infoSystemIdToInfoSystem( taskDto.getInfoSystemId() ) );
        task.setId( taskDto.getId() );
        task.setName( taskDto.getName() );
        task.setCreated( taskDto.getCreated() );
        task.setLaborcosts( taskDto.getLaborcosts() );
        task.setPriority( taskDto.getPriority() );
        task.setDescription( taskDto.getDescription() );

        return task;
    }
}
