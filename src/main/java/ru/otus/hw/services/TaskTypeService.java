package ru.otus.hw.services;


import ru.otus.hw.models.TaskType;
import ru.otus.hw.models.dto.TaskTypeDto;

import java.util.List;
import java.util.Optional;

public interface TaskTypeService {
    Optional<TaskType> findById(long id);

    Optional<TaskType> findByName(String name);

    List<TaskType> findAll();

    TaskType insert(TaskTypeDto taskTypeDto);

    TaskType update(TaskTypeDto taskTypeDto);

    void deleteById(long id);

}
