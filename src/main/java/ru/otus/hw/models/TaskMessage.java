package ru.otus.hw.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.otus.hw.models.dto.TaskDto;

@Data
@AllArgsConstructor
public class TaskMessage {

    private TaskDto task;

    private boolean isNewTask;

    private boolean isDeleteTask;
}
