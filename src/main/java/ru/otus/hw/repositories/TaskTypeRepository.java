package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.TaskType;

import java.util.List;
import java.util.Optional;

public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {

    Optional<TaskType> findByName(@Nonnull String name);

    @Nonnull
    Optional<TaskType> findById(@Nonnull Long id);

    List<TaskType> findAll();

    @Nonnull
    List<TaskType> findAllById(@Nonnull Iterable<Long> id);

    void deleteById(@Nonnull Long id);

}
