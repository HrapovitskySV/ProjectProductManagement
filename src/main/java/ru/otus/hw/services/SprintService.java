package ru.otus.hw.services;

import org.springframework.data.repository.query.Param;
import ru.otus.hw.models.Sprint;
import ru.otus.hw.models.dto.SprintDto;
import ru.otus.hw.models.dto.SprintDtoInputWeb;

import java.util.List;
import java.util.Optional;

public interface SprintService {

    Optional<Sprint> findById(long id);

    Optional<SprintDto> findByIdToDto(long id);

    List<SprintDto> findAll();

    Sprint insert(SprintDtoInputWeb sprintDto);

    Sprint update(SprintDtoInputWeb sprintDto);

    Sprint save(@Param("sprint")Sprint sprint, boolean isNewSprint);

    void deleteById(long id);

    void delete(Sprint sprint);

}
