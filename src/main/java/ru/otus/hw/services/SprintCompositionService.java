package ru.otus.hw.services;

import ru.otus.hw.models.SprintComposition;
import ru.otus.hw.models.dto.SprintCompositionDto;

import java.util.List;
import java.util.Optional;

public interface SprintCompositionService {

    Optional<SprintComposition> findById(long id);

    Optional<SprintCompositionDto> findByIdToDto(long id);

    List<SprintCompositionDto> findAllBySprintId(long sprintId);

    List<SprintCompositionDto> findAll();



    void deleteById(long id);

    void delete(SprintComposition sprintComposition);

}
