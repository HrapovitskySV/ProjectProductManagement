package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import ru.otus.hw.models.CustomUser;

import java.util.List;
import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<CustomUser, Long> {

    @EntityGraph(attributePaths = {"roles"})
    Optional<CustomUser> findByUsername(String username);

    @Nonnull
        //@EntityGraph(attributePaths = "product")
    List<CustomUser> findAll();

    Optional<CustomUser> findById(long id);

    @Nonnull
    CustomUser save(@Nonnull CustomUser user);

    void deleteById(long id);
}

