package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.AvailableUserProduct;
import ru.otus.hw.models.CustomUser;

import java.util.List;

public interface AvailableUserProductRepository extends JpaRepository<AvailableUserProduct, Long> {

    @Nonnull
    List<AvailableUserProduct> findAll();

    @EntityGraph(attributePaths = {"product"})
    List<AvailableUserProduct> findAllByUser(CustomUser user);

    @Nonnull
    List<AvailableUserProduct> findAllById(@Nonnull Iterable<Long> id);

    void delete(@Nonnull AvailableUserProduct availableUserProduct);
}
