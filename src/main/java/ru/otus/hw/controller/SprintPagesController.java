package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.exceptions.SprintNotFoundException;
import ru.otus.hw.models.Product;
import ru.otus.hw.models.dto.SprintDto;
import ru.otus.hw.services.ProductService;
import ru.otus.hw.services.SprintService;
import ru.otus.hw.services.UserService;


import java.util.List;

@Controller()
@RequiredArgsConstructor
public class SprintPagesController {

    private final ProductService productService;


    private final SprintService sprintService;

    private final UserService userService;

    @GetMapping("/sprints")
    public String listAllTasks(Model model) {

        model.addAttribute("currentUser", userService.getCurrentUserDto());
        return "sprintList";
    }


    @GetMapping("/sprints/edit/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        SprintDto sprint = sprintService.findByIdToDto(id).orElseThrow(SprintNotFoundException::new);
        model.addAttribute("sprint", sprint);
        model.addAttribute("method", "PUT");
        return openEditPage(model);
    }


    @GetMapping("/sprints/add")
    public String addPage(Model model) {
        SprintDto sprint = new SprintDto();
        model.addAttribute("sprint", sprint);
        model.addAttribute("method", "POST");
        return openEditPage(model);
    }

    private String openEditPage(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);

        model.addAttribute("redirectUrl", "/sprints");

        return "sprintEdit";
    }

}
