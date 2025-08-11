package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class CommentRepositoryTest {

    @Autowired
    private CommentRepository repositoryComment;

    private List<Product> dbProducts;

    private List<CustomUser> dbUsers;

    private List<Task> dbTasks;

    private List<Comment> dbComments;

    @Autowired
    private TestEntityManager tem;


    @BeforeEach
    void setUp() {
        dbUsers = getDbUsers();
        for (var user: dbUsers) {
            tem.persist(user);
        }
        dbProducts = getDbProducts();
        for (var product: dbProducts) {
            tem.persist(product);
        }
        dbTasks = getDbTasks(dbProducts, dbUsers);
        for (var task: dbTasks) {
            tem.persist(task);
        }

        dbComments = getDbComments(dbTasks);
        for (Comment comment: dbComments) {
            tem.persist(comment);
        }
    }


    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        for (Comment expectedComment: dbComments) {
            var actualComment = repositoryComment.findById(expectedComment.getId());
            assertThat(actualComment).isPresent()
                    .get()
                    .isEqualTo(expectedComment);
        }
    }

    @DisplayName("должен сохранять новый комментарий")
    @Test
    void shouldSaveNewComment() {
        var expectedComment = new Comment(0, dbTasks.get(0), LocalDateTime.now(), "Comment_100",null);
        var returnedComment = repositoryComment.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);

        var actualComment = Optional.ofNullable(tem.find(Comment.class, returnedComment.getId()));

        assertThat(actualComment)
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен сохранять измененный комментари")
    @Test
    void shouldSaveUpdatedComment() {
        long id = dbComments.get(0).getId();

        var expectedComment = new Comment(id, dbTasks.get(0), LocalDateTime.now(), "Comment_100",null);

        var actualComment = Optional.ofNullable(tem.find(Comment.class, id));

        assertThat(actualComment)
                .isPresent()
                .get()
                .isNotEqualTo(expectedComment);

        var returnedComment = repositoryComment.save(expectedComment);
        assertThat(returnedComment).isNotNull()
                .matches(comment -> comment.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedComment);


        actualComment = Optional.ofNullable(tem.find(Comment.class, id));
        assertThat(actualComment)
                .isPresent()
                .get()
                .isEqualTo(returnedComment);
    }

    @DisplayName("должен удалять комментарий по id ")
    @Test
    void shouldDeleteComment() {
        long id = dbComments.get(0).getId();
        var actualComment = Optional.ofNullable(tem.find(Comment.class, id));
        assertThat(actualComment).isPresent();

        repositoryComment.deleteById(id);

        actualComment = Optional.ofNullable(tem.find(Comment.class, id));
        assertThat(actualComment).isEmpty();
    }

    private static List<Product> getDbProducts() {
        return IntStream.range(1, 10).boxed()
                .map(id -> new Product(0, "Product_" + (id+100)))
                .toList();
    }

    private static List<InfoSystem> getInfoSystems() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new InfoSystem(0, "InfoSystem_" + (id+100),false,"",null))
                .toList();
    }

    private static List<CustomUser> getDbUsers() {
        return IntStream.range(1, 10).boxed()
                .map(id -> new CustomUser(0, "User_" + (id+100),"","",false,List.of()))
                .toList();
    }

    private static List<Task> getDbTasks(List<Product> dbProducts, List<CustomUser> dbUsers) {
        List<Task> tasks= IntStream.range(1, 10).boxed()
                .map(id -> new Task(0,
                        "Task" + (id+100),
                        LocalDateTime.now(),
                        new BigDecimal(0),
                        new BigDecimal(0),
                        dbProducts.get(id - 1),
                        null,
                        dbUsers.get(id - 1),
                        dbUsers.get(id - 1),
                        dbUsers.get(id - 1),
                        dbUsers.get(id - 1),
                        null,
                        ""
                ))
                .toList();


        return tasks;
    }

    private static List<Comment> getDbComments(List<Task> dbTasks) {
        return IntStream.range(1, 7).boxed()
                .map(id -> new Comment(0, dbTasks.get(id), LocalDateTime.now(), "Comment_" + id,null))
                .toList();
    }

    private static List<Task> getDbTasks() {
        var dbProducts = getDbProducts();
        var dbUsers = getDbUsers();
        return getDbTasks(dbProducts, dbUsers);
    }

    private static List<Comment> getDbComments() {
        var dbTasks = getDbTasks();
        return getDbComments(dbTasks);
    }
}