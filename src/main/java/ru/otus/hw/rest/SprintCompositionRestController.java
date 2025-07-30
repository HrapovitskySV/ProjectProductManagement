package ru.otus.hw.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.dto.SprintCompositionDto;
import ru.otus.hw.services.SprintCompositionService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SprintCompositionRestController {

    private final SprintCompositionService sprintCompositionService;


    @DeleteMapping("/api/sprintcompositions/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        sprintCompositionService.deleteById(id);
        return ResponseEntity.ok("deleted");
    }

    @GetMapping("/api/sprints/tasks/{sprintId}")
    public List<SprintCompositionDto> getTaskBySprint(@PathVariable("sprintId") long sprintId) {
        var sprintCompositionsDto = sprintCompositionService.findAllBySprintId(sprintId);
        return sprintCompositionsDto;
    }

}
