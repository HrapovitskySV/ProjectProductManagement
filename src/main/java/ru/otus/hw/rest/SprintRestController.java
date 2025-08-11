package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.exceptions.SprintNotFoundException;
import ru.otus.hw.models.Sprint;
import ru.otus.hw.models.dto.SprintDto;
import ru.otus.hw.models.dto.SprintDtoInputWeb;
import ru.otus.hw.services.SprintService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SprintRestController {

    private final SprintService sprintService;


    @GetMapping("/api/sprints")
    public List<SprintDto> listAllSprints() {
        return sprintService.findAll();
    }

    @GetMapping("/api/sprints/{id}")
    public SprintDto getSprint(@PathVariable("id") long id) {
        return sprintService.findByIdToDto(id).orElseThrow(SprintNotFoundException::new);
    }

    @PutMapping("/api/sprints")
    public ResponseEntity<String> saveSprint(@Valid @RequestBody SprintDtoInputWeb sprintDto) {
        var savedSprint = sprintService.update(sprintDto);
        return ResponseEntity.ok("savedSprint");
    }

    @PostMapping("/api/sprints")
    public ResponseEntity<Sprint> insertSprint(@Valid @RequestBody SprintDtoInputWeb sprintDto) {
        var savedSprint = sprintService.insert(sprintDto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(savedSprint);
    }

    @DeleteMapping("/api/sprints/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        sprintService.deleteById(id);
        return ResponseEntity.ok("deleted");
    }



}
