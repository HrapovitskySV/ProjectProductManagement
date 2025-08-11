package ru.otus.hw.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.ProductMapper;
import ru.otus.hw.exceptions.ProductNotFoundException;
import ru.otus.hw.models.Product;
import ru.otus.hw.repositories.ProductRepository;
import ru.otus.hw.security.CustomMutableAclService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.springframework.util.CollectionUtils.isEmpty;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    private final CustomMutableAclService customMutableAclService;

    @Override
    @PostAuthorize("returnObject.isPresent() ? hasPermission(returnObject.get(), 'READ') : true")
    @Transactional(readOnly = true)
    public Optional<Product> findById(long id) {
         return productRepository.findById(id);
    }

    @Override
    @PostAuthorize("returnObject.isPresent() ? hasPermission(returnObject.get(), 'READ') : true")
    public Optional<Product> findByName(String name) {
        var r = productRepository.findByName(name);
        return r;
    }


    @Override
    @PostFilter("hasPermission(filterObject, 'READ')")
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        var r = productRepository.findAll();
        return r;
    }


    @Override
    @Transactional()
    @PreAuthorize("hasRole('ADMIN')")
    public Product insert(String name) {
        var savedObj = save(0, name);

        customMutableAclService.createPermissionOnNewObject(savedObj);

        return savedObj;
    }

    @Override
    @Transactional()
    @PreAuthorize("canUpdate(#id, Product.class)")
    public Product update(@Param("id")long id, String name) {
        return save(id, name);
    }


    @Override
    @Transactional()
    @PreAuthorize("hasPermission(#id, Product.class, 'DELETE')")
    public void deleteById(@Param("id")long id) {
        productRepository.deleteById(id);
    }


    @Override
    @PreAuthorize("hasPermission(#product, 'WRITE')")
    public Product save(@Param("product")Product product) {
        return productRepository.save(product);
    }


    private Product save(long id, String name) {
        var product = new Product(id, name);
        return save(product);
    }


    public List<Product> findAllByIdWithsCheck(Set<Long> productsIds) {
        if (productsIds.size() == 0) {
            return new ArrayList<Product>();
        }
        var products = productRepository.findAllById(productsIds);

        if (isEmpty(products) || productsIds.size() != products.size()) {
            throw new ProductNotFoundException("One or all products with ids %s not found".formatted(productsIds));
        }

        return products;
    }
}
