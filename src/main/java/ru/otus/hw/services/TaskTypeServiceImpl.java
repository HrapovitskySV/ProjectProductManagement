package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.TaskTypeMapper;
import ru.otus.hw.models.TaskType;
import ru.otus.hw.models.dto.TaskTypeDto;
import ru.otus.hw.repositories.TaskTypeRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskTypeServiceImpl implements TaskTypeService {

    private final TaskTypeRepository taskTypeRepository;

    private final TaskTypeMapper taskTypeMapper;

    @Override
    @Cacheable("TaskType")
    public Optional<TaskType> findById(long id) {
        return taskTypeRepository.findById(id);
    }

    @Override
    @Cacheable("TaskType")
    public Optional<TaskType> findByName(String name) {
        return taskTypeRepository.findByName(name);
    }

    @Override
    @Cacheable("TaskType")
    public List<TaskType> findAll() {
        return taskTypeRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "TaskType", allEntries = true)
    public TaskType insert(TaskTypeDto taskTypeDto) {
        TaskType taskType = taskTypeMapper.toModel(taskTypeDto);
        taskType.setId(0);
        return taskTypeRepository.save(taskType);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "TaskType", allEntries = true)
    public TaskType update(TaskTypeDto taskTypeDto) {
        TaskType taskType = taskTypeMapper.toModel(taskTypeDto);
        return taskTypeRepository.save(taskType);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "TaskType", allEntries = true)
    public void deleteById(long id) {
        taskTypeRepository.deleteById(id);
    }


}
