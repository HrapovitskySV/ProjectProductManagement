package ru.otus.hw.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.config.ChangeTaskFlowConfig;
import ru.otus.hw.converters.AbstractTaskMapper;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.Task;
import ru.otus.hw.models.TaskMessage;
import ru.otus.hw.models.dto.TaskDto;
import ru.otus.hw.models.dto.TaskDtoInputWeb;
import ru.otus.hw.repositories.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final AbstractTaskMapper taskMapper;

    private final ChangeTaskFlowConfig.TaskFlowGateway taskFlowGateway;

    @Override
    @PostAuthorize("returnObject.isPresent() ? hasPermission(returnObject.get(), 'READ') : true")
    @Transactional(readOnly = true)
    public Optional<Task> findById(long id) {
        var task = taskRepository.findById(id);
        return task;
    }

    @Override
    @PostAuthorize("returnObject.isPresent() ? hasPermission(returnObject.get(), 'READ') : true")
    @Transactional(readOnly = true)
    // возвращаю Dto т.к. не моугт сериализовать Roles в пользователях. поэтому необходимо возвращать UserDto
    public Optional<TaskDto> findByIdToDto(long id) {
        var task = taskRepository.findById(id).map(taskMapper::toDto);
        return task;
    }


    @Override
    @PostFilter("hasPermission(filterObject, 'READ')")
    @Transactional(readOnly = true)
    // возвращаю Dto т.к. не моугт сериалиховать Roles в пользователях. поэтому необходимо возвращать UserDto
    public List<TaskDto> findAll() {
        var tasks = taskRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        //почему-то возвращает иммутабельный лист и фильтр не может его отфильтровать
        //var tasksDto = taskRepository.findAll().stream().map(taskMapper::toDto).toList();

        List<TaskDto> tasksDto = new ArrayList<>();
        for (var task: tasks) {
            tasksDto.add(taskMapper.toDto(task));
        }

        return tasksDto;
    }


    @Override
    @Transactional()
    @PreAuthorize("hasPermission(#taskDto, 'READ')")
    public Task insert(@Param("taskDto") TaskDtoInputWeb taskDto) {
        return save(taskDto, true);
    }


    @Override
    @Transactional()
    @PreAuthorize("hasPermission(#taskDto, 'READ')")
    public Task update(@Param("taskDto") TaskDtoInputWeb taskDto) {
        return save(taskDto, false);
    }

    @Override
    @Transactional()
    public void deleteById(long id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            delete(task.get());
        }
    }

    @Override
    @PreAuthorize("hasPermission(#task, 'DELETE')")
    @Transactional()
    public void delete(@Param("task")Task task) {
        TaskDto taskDto = taskMapper.toDto(task);
        taskRepository.delete(task);

        taskFlowGateway.changeTask(new TaskMessage(taskDto, false,true));
    }

    @Override
    @PreAuthorize("hasPermission(#task, 'WRITE')")
    public Task save(@Param("task")Task task, boolean isNewTask) {
        if (isNewTask) {
            var curUser = SecurityContextHolder.getContext().getAuthentication();
            task.setAuthor((CustomUser) curUser.getPrincipal());

            task.setId(0);
            task.setCreated(LocalDateTime.now());
        }

        var savedTask = taskRepository.save(task);

        TaskDto taskDto = taskMapper.toDto(task);
        taskFlowGateway.changeTask(new TaskMessage(taskDto, isNewTask,false));

        return savedTask;
    }

    private Task save(TaskDtoInputWeb taskDto, boolean isNewTask) {
        Task task = taskMapper.toModel(taskDto);

        return save(task, isNewTask);
    }
}
