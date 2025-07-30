package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.exceptions.RoleNotFoundException;
import ru.otus.hw.models.Product;
import ru.otus.hw.services.ProductService;
import ru.otus.hw.services.UserService;

@Controller
@RequiredArgsConstructor
public class ProductPagesController {

    private final ProductService productService;

    private final UserService userService;

    @GetMapping("/products")
    public String listAllProducts(Model model) {

        model.addAttribute("currentUser", userService.getCurrentUserDto());

        return "productList";
    }

    @GetMapping("/products/edit/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {

        Product product = productService.findById(id).orElseThrow(RoleNotFoundException::new);
        model.addAttribute("product", product);
        model.addAttribute("method", "PUT");
        model.addAttribute("redirectUrl", "/products");
        model.addAttribute("method", "PUT");
        model.addAttribute("redirectUrl", "/");

        return "productEdit";
    }


    @GetMapping("/products/add")
    public String addPage(Model model) {
        Product product = new Product(0,"");

        model.addAttribute("product", product);
        model.addAttribute("method", "POST");
        model.addAttribute("redirectUrl", "/products");

        return "productEdit";
    }
}
