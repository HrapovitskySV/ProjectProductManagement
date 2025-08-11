package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.Sprint;

import java.util.List;
import java.util.Optional;

public interface SprintRepository extends JpaRepository<Sprint, Long> {

    @EntityGraph(attributePaths = {"product"})
    Optional<Sprint> findByName(@Nonnull String name);

    @Nonnull
    @EntityGraph(attributePaths = {"product"})
    Optional<Sprint> findById(@Nonnull Long id);

    @EntityGraph(attributePaths = {"product"})
    List<Sprint> findAll();

    @Nonnull
    @EntityGraph(attributePaths = {"product"})
    List<Sprint> findAll(Sort sort);


    @Nonnull
    @EntityGraph(attributePaths = {"product"})
    List<Sprint> findAllById(@Nonnull Iterable<Long> id);

    void deleteById(@Nonnull Long id);

}
