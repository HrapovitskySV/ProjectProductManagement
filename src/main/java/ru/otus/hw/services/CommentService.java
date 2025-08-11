package ru.otus.hw.services;

import ru.otus.hw.models.Comment;
import ru.otus.hw.models.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(long id);

    List<CommentDto> findAllByTaskId(Long taskId);

    Comment insert(CommentDto commentDto);

    Comment update(CommentDto commentDto);

    void deleteById(long id);
}
