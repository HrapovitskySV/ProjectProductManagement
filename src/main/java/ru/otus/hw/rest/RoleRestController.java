package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.hw.exceptions.RoleNotFoundException;
import ru.otus.hw.models.Role;
import ru.otus.hw.models.dto.RoleDto;
import ru.otus.hw.services.RoleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleRestController {

    private final RoleService roleService;


    @GetMapping("/api/roles")
    public List<Role> listAllRole() {
        return roleService.findAll();
    }

    @GetMapping("/api/roles/{id}")
    public Role getRole(@PathVariable("id") long id) {
        return roleService.findById(id).orElseThrow(RoleNotFoundException::new);
    }

    @PutMapping("/api/roles")
    public ResponseEntity<String> saveProduct(@Valid  @RequestBody RoleDto roleDto) {
        var savedRole = roleService.update(
                roleDto.getId(),
                roleDto.getName());
        return ResponseEntity.ok("savedRole");
    }

    @PostMapping("/api/roles")
    public ResponseEntity<Role> insertBook(@Valid @RequestBody RoleDto roleDto) {
        var savedRole = roleService.insert(roleDto.getName());
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(savedRole);
    }

    @DeleteMapping("/api/roles/{id}")
    public ResponseEntity<String> deletePage(@PathVariable("id") long id) {
        roleService.deleteById(id);
        return ResponseEntity.ok("deleted");
    }
}
