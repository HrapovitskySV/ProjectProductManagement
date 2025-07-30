package ru.otus.hw.security;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import ru.otus.hw.models.AvailableUserProduct;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.Sprint;
import ru.otus.hw.models.dto.SprintDto;
import ru.otus.hw.models.Task;
import ru.otus.hw.models.dto.SprintDtoInputWeb;
import ru.otus.hw.models.dto.TaskDto;
import ru.otus.hw.models.dto.TaskDtoInputWeb;

public class AclMethodSecurityExpressionRoot extends SecurityExpressionRoot
                                            implements AclMethodSecurityExpressionOperations {

    private Object filterObject;

    private Object returnObject;

    private Object target;

    public AclMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    void setThis(Object target) {
        this.target = target;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        Object element;
        if (filterObject instanceof HibernateProxy) {
            HibernateProxy hibernateProxy = (HibernateProxy)filterObject;
            LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
            element = initializer.getImplementation();
        } else {
            element = filterObject;
        }

        this.filterObject = getProductObject(element);
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public boolean isAdministrator(Object targetId, Class<?> targetClass) {

        return isGranted(targetId, targetClass, admin);
    }

    @Override
    public boolean isAdministrator(Object target) {

        return hasPermission(target, admin);
    }

    @Override
    public boolean canRead(Object targetId, Class<?> targetClass) {

        if (isAdministrator(targetId, targetClass)) {
            return true;
        }

        if (targetId instanceof Task) {
            return isGranted(targetId, targetClass, read);//.getProduct().getId()
        } else {
            return isGranted(targetId, targetClass, read);
        }
    }

    @Override
    public boolean canUpdate(Object targetId, Class<?> targetClass) {
        return isGranted(targetId, targetClass, write);
    }



    boolean isGranted(Object targetId, Class<?> targetClass, Object permission) {

        return hasPermission(targetId, targetClass.getCanonicalName(), permission);
    }

    @Override
    public boolean hasPermission(Object target, Object permission) {
        if (target instanceof TaskDtoInputWeb) {
            // здесь getProductId возвращает ProductId
            return super.hasPermission(((TaskDtoInputWeb) target).getProductId(),
                    "ru.otus.hw.models.Product", permission);
        } else if (target instanceof SprintDtoInputWeb) {
                // здесь getProductId возвращает ProductId
            return super.hasPermission(((SprintDtoInputWeb) target).getProductId(),
                    "ru.otus.hw.models.Product", permission);
        } else {
            return super.hasPermission(getProductObject(target), permission);
        }
    }

    public Object getProductObject(Object target) {
        if (target instanceof TaskDto) {
            // здесь getProduct возвращает Product
            return ((TaskDto) target).getProduct();
        } else if (target instanceof Task) {
            // здесь getProduct возвращает Product
            return ((Task) target).getProduct();
        } else if (target instanceof Sprint) {
            // здесь getProduct возвращает Product
            return ((Sprint) target).getProduct();
        } else if (target instanceof SprintDto) {
            // здесь getProduct возвращает Product
            return ((SprintDto) target).getProduct();
        } else if (target instanceof AvailableUserProduct) {
            return (Product)((AvailableUserProduct) target).getProduct();

        } else {
            return target;
        }
    }
}
