package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.otus.hw.exceptions.RoleNotFoundException;
import ru.otus.hw.models.Role;
import ru.otus.hw.services.RoleService;
import ru.otus.hw.services.UserService;

@Controller
@RequiredArgsConstructor
public class RolePagesController {


    private final RoleService roleService;

    private final UserService userService;

    @GetMapping("/roles")
    public String listAllRoles(Model model) {
        model.addAttribute("currentUser", userService.getCurrentUserDto());

        return "roleList";
    }

    @GetMapping("/roles/edit/{id}")
    public String editPage(@PathVariable("id") long id, Model model) {
        Role role = roleService.findById(id).orElseThrow(RoleNotFoundException::new);
        model.addAttribute("role", role);
        model.addAttribute("method", "PUT");
        model.addAttribute("redirectUrl", "/roles");


        return "roleEdit";
    }


    @GetMapping("/roles/add")
    public String addPage(Model model) {
        Role role = new Role(0,"");

        model.addAttribute("role", role);
        model.addAttribute("method", "POST");
        model.addAttribute("redirectUrl", "/roles");

        return "roleEdit";
    }
}
