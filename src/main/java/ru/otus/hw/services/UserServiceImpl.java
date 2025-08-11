package ru.otus.hw.services;


import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AbstractUserConverter;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.AvailableUserProduct;
import ru.otus.hw.models.CustomUser;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.dto.UserDto;
import ru.otus.hw.models.dto.UserDtoFull;
import ru.otus.hw.models.dto.UserToListDto;
import ru.otus.hw.repositories.CustomUserRepository;
import ru.otus.hw.security.CustomMutableAclService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final CustomUserRepository userRepository;

    private final AvailableUserProductService availableUserProductService;

    private final AbstractUserConverter userMapper;


    private final ProductService productService;

    private final CustomMutableAclService customMutableAclService;

    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ADMIN')")
    public Optional<UserDtoFull> findByIdDtoFull(long id) {
        var oUser = userRepository.findById(id);
        var oUserDto = oUser.map(userMapper::toDtoFull);;
        if (oUser.isEmpty()) {
            return oUserDto;
        }

        var userDto = oUserDto.get();

        List<Product> products = availableUserProductService.findAllByUser(oUser.get()).
                                                stream().map(ap -> ap.getProduct()).toList();
        userDto.setProducts(products);

        return oUserDto;
    }


    @Override
    @Transactional(readOnly = true)
    @Cacheable("CustomUser")
    public List<UserToListDto> findAlltoList() {
        //return userRepository.findAll().stream().map(userMapper::toListDto).toList();

        var users = userRepository.findAll();
        List<UserToListDto> usersDto = new ArrayList<>();
        for (var user: users) {
            usersDto.add(userMapper.toListDto(user));
        }

        return usersDto;
    }

    @Override
    @Cacheable("CustomUser")
    public Optional<CustomUser> findById(long id) {
        return userRepository.findById(id);
    }


    @Override
    @Transactional()
    @CacheEvict(value = "CustomUser", allEntries = true)
    @PreAuthorize("hasRole('ADMIN')")
    public CustomUser insert(UserDto userDto) {
        var user = userMapper.toModel(userDto);
        user.setId(0);

        user.setPassword(passwordEncoder.encode(userDto.getInputPassword()));
        var savedUser = userRepository.save(user);

        // создаю новый Sid в Acl правах
        customMutableAclService.createOrRetrieveSidPrimaryKey(savedUser.getUsername(),true, true);

        var products = productService.findAllByIdWithsCheck(userDto.getProducts());

        for (var product: products) {
            availableUserProductService.save(new AvailableUserProduct(0, savedUser, product));

            customMutableAclService.createPermission(product,savedUser.getUsername(),true, List.of(BasePermission.READ));
        }

        return savedUser;
    }

    @Transactional()
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "CustomUser", allEntries = true)
    @Override
    public CustomUser update(UserDto userDto) {
        var oUser = userRepository.findById(userDto.getId());
        if (oUser.isEmpty()) {
            throw new EntityNotFoundException("User with id %s not found".formatted(userDto.getId()));
        }

        ///  еще старый пользователь
        var user = oUser.get();
        String oldNameUser = user.getUsername();
        //получаю продукты по ИД с проверкой
        var products = productService.findAllByIdWithsCheck(userDto.getProducts());
        availableUserProductService.updateAvailableUserProducts(user, products);

        user = userMapper.toModel(userDto);
        ///  уже новый пользователь, но не записанный
        /// если пароль пустой, оставляю старый пароль, иначе кодирую новый пароль
        if (!Objects.equals(userDto.getInputPassword(), "")) {
            String encodePassword = passwordEncoder.encode(userDto.getInputPassword());
            user.setPassword(encodePassword);
        } else {
            user.setPassword(oUser.get().getPassword());
        }

        var savedUser = userRepository.save(user);

        ///если изменилось имя пользователя меняб sid в acl
        /// последовательность важ
        if (!Objects.equals(oldNameUser, user.getUsername())) {
            customMutableAclService.updateSidPrimaryKey(oldNameUser, user.getUsername(), true);
        }

        return savedUser;
    }




    @Override
    @Transactional()
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "CustomUser", allEntries = true)
    public void deleteById(@Param("id")long id) {
        userRepository.deleteById(id);
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "CustomUser", allEntries = true)
    public CustomUser save(@Param("task")CustomUser user) {
        return userRepository.save(user);
    }

    @Override
    public UserToListDto getCurrentUserDto() {
        var currentUser = (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userMapper.toListDto(currentUser);
    }

    @Override
    public CustomUser getCurrentUser() {
        return (CustomUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    @Cacheable("CustomUser")
    public Optional<CustomUser> findByUsername(String userName) {
            return userRepository.findByUsername(userName);
    }
}
