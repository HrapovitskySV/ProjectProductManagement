package ru.otus.hw.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AbstractCommentMapperImpl;
import ru.otus.hw.models.dto.CommentDto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("Сервис для работы с комментариями ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({CommentServiceImpl.class, AbstractCommentMapperImpl.class, UserService.class, TaskService.class})//CommentRepository.class
@Transactional(propagation = Propagation.NEVER)
class CommentServiceImplTest {


    @Autowired
    private CommentService commentService;

    @MockBean
    private UserService userService;

    @MockBean
    private TaskService taskService;

    @DisplayName("У комментария должен получить задачу")
    @Test
    void findById() {
        var actualComment = commentService.findById(1L);
        assertDoesNotThrow(() -> actualComment.get().getTaskId());
    }

    @Test
    void findAllByTaskId() {
        var actualComments = commentService.findAllByTaskId(1L);

        assertDoesNotThrow(() -> actualComments.stream().map(CommentDto::getTaskId).toList());
    }





}