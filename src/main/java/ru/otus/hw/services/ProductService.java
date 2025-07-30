package ru.otus.hw.services;

import ru.otus.hw.models.Product;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductService {
    Optional<Product> findById(long id);

    Optional<Product> findByName(String name);

    List<Product> findAll();

    Product insert(String name);

    Product update(long id, String name);

    Product save(Product product);

    void deleteById(long id);

    List<Product> findAllByIdWithsCheck(Set<Long> productsIds);
}
