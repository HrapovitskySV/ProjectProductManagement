package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Nonnull
    List<Product> findAll();

    @Query(value = "select p.* " +
            "from products p " +
            "   inner join users_products up on p.id=up.product_id " +
            "where up.user_id = :userId",
            nativeQuery = true)
    List<Product> findAllByUser(@Param("userId") Long userId);

    @Nonnull
    Optional<Product> findByName(String name);

    @Nonnull
    List<Product> findAllById(@Nonnull Iterable<Long> id);
}
