package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.SprintComposition;

import java.util.List;
import java.util.Optional;

public interface SprintCompositionRepository extends JpaRepository<SprintComposition, Long> {

    @Nonnull
    @EntityGraph(attributePaths = {"sprint","sprint.product","task"})
    Optional<SprintComposition> findById(@Nonnull Long id);

    @EntityGraph(attributePaths = {"sprint","sprint.product","task"})
    List<SprintComposition> findAll();

    @Nonnull
    @EntityGraph(attributePaths = {"sprint","sprint.product","task"})
    List<SprintComposition> findAllBySprintId(@Nonnull Long id);


    @Nonnull
    @EntityGraph(attributePaths = {"sprint","sprint.product","task"})
    List<SprintComposition> findAllById(@Nonnull Iterable<Long> id);

    void deleteById(@Nonnull Long id);

}
