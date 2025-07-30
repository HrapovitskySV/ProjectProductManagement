package ru.otus.hw.services;

import ru.otus.hw.models.Task;
import ru.otus.hw.models.dto.TaskDto;
import ru.otus.hw.models.dto.TaskDtoInputWeb;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    Optional<Task> findById(long id);

    Optional<TaskDto> findByIdToDto(long id);

    List<TaskDto> findAll();

    Task insert(TaskDtoInputWeb taskDto);

    Task update(TaskDtoInputWeb taskDto);

    void deleteById(long id);

    void delete(Task task);

    Task save(Task task, boolean isNewTask);
}
