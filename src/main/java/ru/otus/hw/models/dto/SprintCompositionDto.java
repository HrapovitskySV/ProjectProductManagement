package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SprintCompositionDto {
    private long id;

    private SprintDto sprint;

    private long taskId;

    private String taskName;

    private String taskTypeName;

    private String taskAuthorName;

    private String taskResponsibleName;

}
