package ru.otus.hw.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.otus.hw.exceptions.InfoSystemNotFoundException;
import ru.otus.hw.models.InfoSystem;
import ru.otus.hw.models.dto.InfoSystemDto;
import ru.otus.hw.services.InfoSystemService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class InfoSystemRestController {

    private final InfoSystemService infoSystemService;


    @GetMapping("/api/infosystems")
    public List<InfoSystem> listAllRole() {
        return infoSystemService.findAll();
    }

    @GetMapping("/api/infosystems/{id}")
    public InfoSystem getInfoSystem(@PathVariable("id") long id) {
        return infoSystemService.findById(id).orElseThrow(InfoSystemNotFoundException::new);
    }

    @PutMapping("/api/infosystems")
    public ResponseEntity<String> saveInfoSystem(@Valid  @RequestBody InfoSystemDto infoSystemDto) {
        var savedInfoSystem = infoSystemService.update(infoSystemDto);
        return ResponseEntity.ok("saved");
    }

    @PostMapping("/api/infosystems")
    public ResponseEntity<InfoSystem> insert(@Valid @RequestBody InfoSystemDto infoSystemDto) {
        var savedInfoSystem = infoSystemService.insert(infoSystemDto);
        return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(savedInfoSystem);
    }

    @DeleteMapping("/api/infosystems/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") long id) {
        infoSystemService.deleteById(id);
        return ResponseEntity.ok("deleted");
    }
}
