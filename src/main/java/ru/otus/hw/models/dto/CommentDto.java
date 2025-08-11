package ru.otus.hw.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentDto {
    private long id;

    private long taskId;

    private LocalDateTime created;

    private String comment;

    private UserToListDto author;
}
