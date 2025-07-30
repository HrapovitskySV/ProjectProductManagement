package ru.otus.hw.rest;

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
import ru.otus.hw.exceptions.CommentNotFoundException;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.services.CommentService;


import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;


    @GetMapping("/api/comments/{id}")
    public CommentDto getComment(@PathVariable("id") long id) {
        return commentService.findById(id).orElseThrow(CommentNotFoundException::new);
    }

    @GetMapping("/api/comments/task/{taskId}")
    public List<CommentDto> getCommentByTask(@PathVariable("taskId") long taskId) {
        return commentService.findAllByTaskId(taskId);
    }

    @PutMapping("/api/comments")
    public ResponseEntity<String> saveComment(@RequestBody CommentDto commentDto) {
        var savedComment = commentService.update(commentDto);
        return ResponseEntity.ok("saved");
    }

    @PostMapping("/api/comments")
    public ResponseEntity<String> insertComment(@RequestBody CommentDto commentDto) {
        commentDto.setId(0);
        var savedComment = commentService.insert(commentDto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body("saved");
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable("id") long id) {
        commentService.deleteById(id);
        return ResponseEntity.ok("deleted");
    }
}
