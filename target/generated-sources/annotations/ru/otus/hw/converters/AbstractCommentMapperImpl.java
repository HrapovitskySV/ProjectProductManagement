package ru.otus.hw.converters;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.Comment;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.Task;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.UserToListDto;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-30T12:10:15+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.15 (Ubuntu)"
)
@Component
public class AbstractCommentMapperImpl extends AbstractCommentMapper {

    @Override
    public CommentDto toDto(Comment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDto commentDto = new CommentDto();

        commentDto.setTaskId( commentTaskId( comment ) );
        commentDto.setId( comment.getId() );
        commentDto.setCreated( comment.getCreated() );
        commentDto.setComment( comment.getComment() );
        commentDto.setAuthor( userToUserToListDto( comment.getAuthor() ) );

        return commentDto;
    }

    @Override
    public Comment toModel(CommentDto commentDto) {
        if ( commentDto == null ) {
            return null;
        }

        Comment comment = new Comment();

        comment.setTask( taskIdToTask( commentDto.getTaskId() ) );
        comment.setId( commentDto.getId() );
        comment.setCreated( commentDto.getCreated() );
        comment.setComment( commentDto.getComment() );
        comment.setAuthor( userToListDtoToCustomUser( commentDto.getAuthor() ) );

        return comment;
    }

    private long commentTaskId(Comment comment) {
        if ( comment == null ) {
            return 0L;
        }
        Task task = comment.getTask();
        if ( task == null ) {
            return 0L;
        }
        long id = task.getId();
        return id;
    }

    protected CustomUser userToListDtoToCustomUser(UserToListDto userToListDto) {
        if ( userToListDto == null ) {
            return null;
        }

        CustomUser customUser = new CustomUser();

        customUser.setId( userToListDto.getId() );
        customUser.setUsername( userToListDto.getUsername() );
        customUser.setEmail( userToListDto.getEmail() );
        customUser.setInformAboutTasks( userToListDto.isInformAboutTasks() );

        return customUser;
    }
}
