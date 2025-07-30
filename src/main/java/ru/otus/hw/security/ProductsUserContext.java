package ru.otus.hw.security;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.otus.hw.models.Product;
import ru.otus.hw.repositories.ProductRepository;

import java.util.List;

import static java.util.Objects.isNull;

@Component
@SessionScope
@RequiredArgsConstructor
public class ProductsUserContext {

    private List<Product> productUserList;

    private final ProductRepository productRepository;

    public List<Product> getProductUserList() {
        if (isNull(productUserList)) {
            refreshProductUserList();
        }
        return productUserList;
    }

    @EventListener
    public void onFailure(AuthorizationGrantedEvent failure) {
        refreshProductUserList();
    }

    @PostConstruct
    public void refreshProductUserList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       // productRepository.findAllByUser(new Long(1));
    }
}
