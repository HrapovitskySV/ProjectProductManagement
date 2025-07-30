package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.exceptions.InfoSystemNotFoundException;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.dto.InfoSystemDto;
import ru.otus.hw.services.InfoSystemService;
import ru.otus.hw.services.ProductService;
import ru.otus.hw.services.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class InfosystemPagesController {

    private final InfoSystemService infoSystemService;

    private final ProductService productService;

    private final UserService userService;

    @GetMapping("/infosystems")
    public String listAllPage(Model model) {
        model.addAttribute("Url", "/infosystems");
        model.addAttribute("currentUser", userService.getCurrentUserDto());

        return "infoSystemList";
    }

    @GetMapping("/infosystems/edit/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        InfoSystemDto infoSystem = infoSystemService.findByIdDto(id).orElseThrow(InfoSystemNotFoundException::new);
        model.addAttribute("infoSystem", infoSystem);
        model.addAttribute("method", "PUT");

        return openEditPage(model);
    }


    @GetMapping("/infosystems/add")
    public String insertInfosystems(Model model) {
        InfoSystemDto infoSystem = new InfoSystemDto(0,"",false,"",0);

        model.addAttribute("infoSystem", infoSystem);
        model.addAttribute("method", "POST");
        return openEditPage(model);
    }

    private String openEditPage(Model model) {
        model.addAttribute("redirectUrl", "/infosystems");

        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        return "infoSystemEdit";
    }
}
