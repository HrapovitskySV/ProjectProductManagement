package ru.otus.hw.converters;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.Task;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.UserToListDto;
import ru.otus.hw.services.UserService;
import ru.otus.hw.services.TaskService;

@Mapper(componentModel = "spring")
public abstract class AbstractCommentMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskService taskService;

    @Mapping(target = "taskId", source = "task.id")
    public abstract CommentDto toDto(Comment comment);

    @Mapping(target = "task", source = "taskId")
    public abstract Comment toModel(CommentDto commentDto);

    public UserToListDto userToUserToListDto(CustomUser user) {
        return Mappers.getMapper(AbstractUserConverter.class).toListDto(user);
    }

    public CustomUser userIdToUser(long userId) {

        if (userId == 0) {
            return null;
        }

        var oUser = userService.findById(userId);
        if (oUser.isEmpty()) {
            throw new EntityNotFoundException("User with id %s not found".formatted(userId));
        }
        return oUser.get();
    }

    public Task taskIdToTask(long taskId) {

        if (taskId == 0) {
            return null;
        }

        var oTask = taskService.findById(taskId);
        if (oTask.isEmpty()) {
            throw new EntityNotFoundException("Task with id %s not found".formatted(taskId));
        }
        return oTask.get();
    }

}
