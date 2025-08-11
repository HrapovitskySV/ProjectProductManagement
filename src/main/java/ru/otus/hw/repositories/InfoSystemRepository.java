package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import ru.otus.hw.models.InfoSystem;

import java.util.List;
import java.util.Optional;

public interface InfoSystemRepository extends JpaRepository<InfoSystem, Long> {

    @EntityGraph(attributePaths = {"primaryProduct"})
    Optional<InfoSystem> findByName(@Nonnull String name);

    @Nonnull
    @EntityGraph(attributePaths = {"primaryProduct"})
    List<InfoSystem> findAllById(@Nonnull Iterable<Long> id);

    @Nonnull
    @EntityGraph(attributePaths = {"primaryProduct"})
    List<InfoSystem> findAll();

    @Nonnull
    @EntityGraph(attributePaths = {"primaryProduct"})
    Optional<InfoSystem> findById(@Nonnull Long id);

    @Nonnull
    List<InfoSystem> findAllByUseWebHook(boolean useWebHook);

    List<InfoSystem> findAllByName(String infoSystemName);
}
