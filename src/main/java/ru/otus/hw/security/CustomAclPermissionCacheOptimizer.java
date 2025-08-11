package ru.otus.hw.security;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.core.Authentication;
import ru.otus.hw.models.AvailableUserProduct;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.Sprint;
import ru.otus.hw.models.Task;
import ru.otus.hw.models.dto.SprintDto;
import ru.otus.hw.models.dto.TaskDto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomAclPermissionCacheOptimizer extends AclPermissionCacheOptimizer {
    public CustomAclPermissionCacheOptimizer(AclService aclService) {
        super(aclService);
    }

    @Override
    public void cachePermissionsFor(Authentication authentication, Collection<?> objects) {
        if (objects.isEmpty()) {
            return;
        }

        Set<Object> products = new HashSet<>();
        Object element;
        var iterator = objects.iterator();
        while (iterator.hasNext()) {
            element = iterator.next();
            if (element instanceof HibernateProxy) {
                HibernateProxy hibernateProxy = (HibernateProxy)element;
                LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
                element = initializer.getImplementation();
            }

            if (element instanceof Task) {
                products.add((Product)((Task) element).getProduct());
            } else if (element instanceof TaskDto) {
                products.add((Product)((TaskDto) element).getProduct());
            } else if (element instanceof AvailableUserProduct) {
                products.add((Product)((AvailableUserProduct) element).getProduct());
            } else if (element instanceof Sprint) {
                products.add((Product)((Sprint) element).getProduct());
            } else if (element instanceof SprintDto) {
                products.add((Product)((SprintDto) element).getProduct());
            } else {
                products.add(element);
            }
        }

        super.cachePermissionsFor(authentication, products);
    }
}
