package ru.otus.hw.services;

import ru.otus.hw.models.AvailableUserProduct;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.Product;

import java.util.List;
import java.util.Optional;


public interface AvailableUserProductService {
    List<AvailableUserProduct> findAll();

    Optional<AvailableUserProduct> findById(long id);

    List<AvailableUserProduct> findAllByUser(CustomUser user);

    AvailableUserProduct save(AvailableUserProduct availableUserProduct);

     void delete(AvailableUserProduct availableUserProduct);

    void updateAvailableUserProducts(CustomUser user, List<Product> newProducts);

    void insertAvailableUserProducts(CustomUser user, List<Product> newProducts);
}