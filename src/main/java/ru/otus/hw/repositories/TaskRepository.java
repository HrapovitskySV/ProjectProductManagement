package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @EntityGraph(attributePaths = {"product",
                                    "infoSystem",
                                    "author",
                                    "responsible",
                                    "programmer",
                                    "analyst",
                                    "taskType"})
    Optional<Task> findById(long id);

    @Nonnull
    @EntityGraph(attributePaths = {"product",
            "infoSystem",
            "author",
            "responsible",
            "programmer",
            "analyst",
            "taskType"})
    List<Task> findAll();

    @Nonnull
    @EntityGraph(attributePaths = {"product",
            "infoSystem",
            "author",
            "responsible",
            "programmer",
            "analyst",
            "taskType"})
    List<Task> findAll(Sort sort);


    @Nonnull
    @EntityGraph(attributePaths = {"product",
            "infoSystem",
            "author",
            "responsible",
            "programmer",
            "analyst",
            "taskType"})
    List<Task> findAllByProduct(Product products);

    @Nonnull
    @EntityGraph(attributePaths = {"product",
            "infoSystem",
            "author",
            "responsible",
            "programmer",
            "analyst",
            "taskType"})
    List<Task> findAllByProductIn(List<Product> products);

    @Nonnull
    Task save(@Nonnull Task task);

    //void deleteById(long id);

    void delete(@Nonnull Task task);
}
