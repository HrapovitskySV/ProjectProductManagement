package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.hw.exceptions.TaskNotFoundException;
import ru.otus.hw.models.Task;
import ru.otus.hw.models.dto.TaskDto;
import ru.otus.hw.models.dto.TaskDtoInputWeb;
import ru.otus.hw.services.LoadTaskFomExcelService;
import ru.otus.hw.services.TaskService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TaskRestController {

    private final TaskService taskService;

    private final LoadTaskFomExcelService loadTaskFomExcelService;


    @GetMapping("/api/tasks")
    public List<TaskDto> listAllTask() {
        var r =  taskService.findAll();
        return r;
    }

    @GetMapping("/api/tasks/{id}")
    public TaskDto getTask(@PathVariable("id") long id) {
        return taskService.findByIdToDto(id).orElseThrow(TaskNotFoundException::new);
    }

    @PutMapping("/api/tasks")
    public ResponseEntity<String> saveTask(@Valid @RequestBody TaskDtoInputWeb taskDto) {
        taskService.update(taskDto);
        return ResponseEntity.ok("saved");
    }

    @PostMapping("/api/tasks")
    public ResponseEntity<String> insertTask(@Valid @RequestBody TaskDtoInputWeb taskDto) {
        Task task = taskService.insert(taskDto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body("saved");
    }

    @DeleteMapping("/api/tasks/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable("id") long id) {
        taskService.deleteById(id);
        return ResponseEntity.ok("deleted");
    }

}
