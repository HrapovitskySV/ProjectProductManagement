package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.exceptions.UserNotFoundException;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.dto.UserDtoFull;
import ru.otus.hw.services.ProductService;
import ru.otus.hw.services.RoleService;
import ru.otus.hw.services.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserPagesController {

    private final UserService userService;

    private final ProductService productService;

    private final RoleService roleService;


    @GetMapping("/users")
    public String listAllUsers(Model model) {
        model.addAttribute("currentUser", userService.getCurrentUserDto());

        return "userList";
    }

    @GetMapping("/users/edit/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        UserDtoFull user = userService.findByIdDtoFull(id).orElseThrow(UserNotFoundException::new);
        model.addAttribute("user", user);



        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);

        model.addAttribute("method", "PUT");
        model.addAttribute("redirectUrl", "/users");


        return "userEdit";
    }


    @GetMapping("/users/add")
    public String addPage(Model model) {
        UserDtoFull user = new UserDtoFull(0,"","",false,"",List.of(),List.of());

        model.addAttribute("user", user);

        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        List<Role> roles = roleService.findAll();
        model.addAttribute("roles", roles);

        model.addAttribute("method", "POST");
        model.addAttribute("redirectUrl", "/users");

        return "userEdit";
    }
}
