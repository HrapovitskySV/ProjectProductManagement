package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.AvailableUserProduct;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.Product;
import ru.otus.hw.repositories.AvailableUserProductRepository;
import ru.otus.hw.security.CustomMutableAclService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class AvailableUserProductServiceImpl implements AvailableUserProductService {

    private final AvailableUserProductRepository availableUserProductRepository;

    private final CustomMutableAclService customMutableAclService;

    @Override
    public List<AvailableUserProduct> findAll() {
        return availableUserProductRepository.findAll();
    }

    @Override
    @PostAuthorize("returnObject.isPresent() ? hasPermission(1, 'ru.otus.hw.models.Product', 'READ') : true")
    public Optional<AvailableUserProduct> findById(long id) {
        return availableUserProductRepository.findById(id);
    }

    @Override
    @PostFilter("hasPermission(filterObject, 'READ')")
    public List<AvailableUserProduct> findAllByUser(CustomUser user) {
        return availableUserProductRepository.findAllByUser(user);
    }

    @Override
    @PostAuthorize("hasPermission(returnObject, 'READ')")
    public AvailableUserProduct save(AvailableUserProduct availableUserProduct) {
        var r = availableUserProductRepository.save(availableUserProduct);
        return r;
    }

    @Override
    public void delete(AvailableUserProduct availableUserProduct) {
        availableUserProductRepository.delete(availableUserProduct);
    }

    @Transactional
    public void insertAvailableUserProducts(CustomUser user, List<Product> newProducts) {
        for (var product: newProducts) {
             save(new AvailableUserProduct(0, user, product));
            // создаю Acl права пользователю на данный продукт
            customMutableAclService.createPermission(product,user.getUsername(), true,List.of(BasePermission.READ));
        }
    }

    @Transactional
    public void updateAvailableUserProducts(CustomUser user, List<Product> newProducts) {

        //создаю Map c ключем продукт для бытрого поиска
        Map<Product, AvailableUserProduct> availableUserProductsMap = findAllByUser(user)
                .stream()
                .collect(Collectors.toMap(val -> val.getProduct(), val -> val));

        AvailableUserProduct availableUserProduct;
        for (var product: newProducts) {

            availableUserProduct = availableUserProductsMap.get(product);

            if (isNull(availableUserProduct)) {
                // если нет создаю и записываю
                save(new AvailableUserProduct(0, user, product));
            } else {
                // если есть удаляю из Map, потом все что останется в Map удалю
                availableUserProductsMap.remove(product);
            }

            // создаю Acl права пользователю на данный продукт
            customMutableAclService.createPermission(product,user.getUsername(),true,List.of(BasePermission.READ));
        }

        // по продуктам оставшимся в Map удаляю AvailableUserProduct и  Acl права пользователю на данный продукт
        for (Map.Entry<Product, AvailableUserProduct> entry : availableUserProductsMap.entrySet()) {
            delete(entry.getValue());
            customMutableAclService.removePermission(
                    new ObjectIdentityImpl(entry.getKey()),
                    user.getUsername());
        }
        availableUserProductsMap.clear();
    }

}
