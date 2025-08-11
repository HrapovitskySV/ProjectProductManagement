package ru.otus.hw.converters;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.SprintComposition;
import ru.otus.hw.models.Task;
import ru.otus.hw.models.TaskType;
import ru.otus.hw.models.dto.SprintCompositionDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T12:10:15+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class AbstractSprintCompositionMapperImpl extends AbstractSprintCompositionMapper {

    @Override
    public SprintCompositionDto toDto(SprintComposition sprintComposition) {
        if ( sprintComposition == null ) {
            return null;
        }

        SprintCompositionDto sprintCompositionDto = new SprintCompositionDto();

        sprintCompositionDto.setTaskId( sprintCompositionTaskId( sprintComposition ) );
        sprintCompositionDto.setTaskName( sprintCompositionTaskName( sprintComposition ) );
        sprintCompositionDto.setTaskTypeName( sprintCompositionTaskTaskTypeName( sprintComposition ) );
        sprintCompositionDto.setTaskAuthorName( sprintCompositionTaskAuthorUsername( sprintComposition ) );
        sprintCompositionDto.setTaskResponsibleName( sprintCompositionTaskResponsibleUsername( sprintComposition ) );
        sprintCompositionDto.setId( sprintComposition.getId() );
        sprintCompositionDto.setSprint( sprintToSprintDto( sprintComposition.getSprint() ) );

        return sprintCompositionDto;
    }

    private long sprintCompositionTaskId(SprintComposition sprintComposition) {
        if ( sprintComposition == null ) {
            return 0L;
        }
        Task task = sprintComposition.getTask();
        if ( task == null ) {
            return 0L;
        }
        long id = task.getId();
        return id;
    }

    private String sprintCompositionTaskName(SprintComposition sprintComposition) {
        if ( sprintComposition == null ) {
            return null;
        }
        Task task = sprintComposition.getTask();
        if ( task == null ) {
            return null;
        }
        String name = task.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String sprintCompositionTaskTaskTypeName(SprintComposition sprintComposition) {
        if ( sprintComposition == null ) {
            return null;
        }
        Task task = sprintComposition.getTask();
        if ( task == null ) {
            return null;
        }
        TaskType taskType = task.getTaskType();
        if ( taskType == null ) {
            return null;
        }
        String name = taskType.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String sprintCompositionTaskAuthorUsername(SprintComposition sprintComposition) {
        if ( sprintComposition == null ) {
            return null;
        }
        Task task = sprintComposition.getTask();
        if ( task == null ) {
            return null;
        }
        CustomUser author = task.getAuthor();
        if ( author == null ) {
            return null;
        }
        String username = author.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }

    private String sprintCompositionTaskResponsibleUsername(SprintComposition sprintComposition) {
        if ( sprintComposition == null ) {
            return null;
        }
        Task task = sprintComposition.getTask();
        if ( task == null ) {
            return null;
        }
        CustomUser responsible = task.getResponsible();
        if ( responsible == null ) {
            return null;
        }
        String username = responsible.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }
}
