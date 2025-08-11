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


@DisplayName("Репозиторий на основе Jpa для работы с информационными системами ")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

class InfoSystemRepositoryTest {

    @Autowired
    private InfoSystemRepository infoSystemRepository;

    private List<InfoSystem> dbInfoSystems;

    @Autowired
    private TestEntityManager tem;


    @BeforeEach
    void setUp() {
        dbInfoSystems = getInfoSystems();
        for (var infoSystem: dbInfoSystems) {
            tem.persist(infoSystem);
        }
    }


    @DisplayName("должен загружать InfoSystem по id")
    @Test
    void shouldReturnCorrectCommentById() {
        for (InfoSystem expectedInfoSystem: dbInfoSystems) {
            var actualComment = infoSystemRepository.findById(expectedInfoSystem.getId());
            assertThat(actualComment).isPresent()
                    .get()
                    .isEqualTo(expectedInfoSystem);
        }
    }

    @DisplayName("должен сохранять новый InfoSystem")
    @Test
    void shouldSaveNewInfoSystem() {
        var expectedInfoSystem = new InfoSystem(0, "InfoSystem_100",false,"",null);
        var returnedInfoSystem = infoSystemRepository.save(expectedInfoSystem);
        assertThat(returnedInfoSystem).isNotNull()
                .matches(infoSystem -> infoSystem.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedInfoSystem);

        var actualInfoSystem = Optional.ofNullable(tem.find(InfoSystem.class, returnedInfoSystem.getId()));

        assertThat(actualInfoSystem)
                .isPresent()
                .get()
                .isEqualTo(returnedInfoSystem);
    }

    @DisplayName("должен сохранять измененную InfoSystem")
    @Test
    void shouldSaveUpdatedInfoSystem() {
        long id = dbInfoSystems.get(0).getId();

        var expectedInfoSystem = new InfoSystem(id, "InfoSystem_100",false,"",null);

        var actualInfoSystem = Optional.ofNullable(tem.find(InfoSystem.class, id));

        assertThat(actualInfoSystem)
                .isPresent()
                .get()
                .isNotEqualTo(expectedInfoSystem);

        var returnedInfoSystem = infoSystemRepository.save(expectedInfoSystem);
        assertThat(returnedInfoSystem).isNotNull()
                .matches(infoSystem -> infoSystem.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedInfoSystem);


        actualInfoSystem = Optional.ofNullable(tem.find(InfoSystem.class, id));
        assertThat(actualInfoSystem)
                .isPresent()
                .get()
                .isEqualTo(returnedInfoSystem);
    }

    @DisplayName("должен удалять InfoSystem по id ")
    @Test
    void shouldDeleteComment() {
        long id = dbInfoSystems.get(0).getId();
        var actualInfoSystem = Optional.ofNullable(tem.find(InfoSystem.class, id));
        assertThat(actualInfoSystem).isPresent();

        infoSystemRepository.deleteById(id);

        actualInfoSystem = Optional.ofNullable(tem.find(InfoSystem.class, id));
        assertThat(actualInfoSystem).isEmpty();
    }

    private static List<InfoSystem> getInfoSystems() {
        return IntStream.range(1, 4).boxed()
                .map(id -> new InfoSystem(0, "InfoSystem_" + (id+100),false,"",null))
                .toList();
    }
}