package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AbstractCommentMapper;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.repositories.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final AbstractCommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDto> findById(long id) {
        Optional<Comment> oComment = commentRepository.findById(id);
        return oComment.map(commentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDto> findAllByTaskId(Long taskId) {
        var comments = commentRepository.findAllByTaskId(taskId);
        return comments.stream().map(commentMapper::toDto).toList();
    }

    @Override
    @Transactional()
    public Comment insert(CommentDto commentDto) {
        return save(commentDto, true);
    }

    @Override
    @Transactional()
    public Comment update(CommentDto commentDto) {
        return save(commentDto, false);
    }

    @Override
    @Transactional()
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private Comment save(CommentDto commentDto, boolean itsNew) {
        var comment = commentMapper.toModel(commentDto);
        if (itsNew) {
            commentDto.setId(0);
            commentDto.setCreated(LocalDateTime.now());

            var curUser = SecurityContextHolder.getContext().getAuthentication();
            comment.setAuthor((CustomUser) curUser.getPrincipal());
        }
        return commentRepository.save(comment);
    }
}
