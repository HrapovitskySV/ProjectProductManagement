package ru.otus.hw.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AbstractSprintCompositionMapper;
import ru.otus.hw.models.SprintComposition;
import ru.otus.hw.models.dto.SprintCompositionDto;
import ru.otus.hw.repositories.SprintCompositionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SprintCompositionServiceImpl implements SprintCompositionService {

    private final SprintCompositionRepository sprintCompositionRepository;

    private final AbstractSprintCompositionMapper sprintCompositionMapper;


    @Override
    //@PostAuthorize("returnObject.isPresent() ? hasPermission(returnObject.get(), 'READ') : true")
    @Transactional(readOnly = true)
    public Optional<SprintComposition> findById(long id) {
        var sprintComposition = sprintCompositionRepository.findById(id);
        return sprintComposition;
    }

    @Override
    //@PostAuthorize("returnObject.isPresent() ? hasPermission(returnObject.get(), 'READ') : true")
    @Transactional(readOnly = true)
    public Optional<SprintCompositionDto> findByIdToDto(long id) {
        var sprintCompositionDto = sprintCompositionRepository.findById(id).map(sprintCompositionMapper::toDto);
        return sprintCompositionDto;
    }

    @Override
    public List<SprintCompositionDto> findAllBySprintId(long sprintId) {
        var sprintCompositions = sprintCompositionRepository.findAllBySprintId(sprintId);
        List<SprintCompositionDto> sprintCompositionsDto = new ArrayList<>();
        for (var sprintComposition: sprintCompositions) {
            sprintCompositionsDto.add(sprintCompositionMapper.toDto(sprintComposition));
        }

        return sprintCompositionsDto;
    }


    @Override
    //@PostFilter("hasPermission(filterObject, 'READ')")
    @Transactional(readOnly = true)
    public List<SprintCompositionDto> findAll() {
        var sprintCompositions = sprintCompositionRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<SprintCompositionDto> sprintCompositionsDto = new ArrayList<>();
        for (var sprintComposition: sprintCompositions) {
            sprintCompositionsDto.add(sprintCompositionMapper.toDto(sprintComposition));
        }

        return sprintCompositionsDto;
    }



    @Override
    @Transactional()
    public void deleteById(long id) {
        Optional<SprintComposition> sprintComposition = sprintCompositionRepository.findById(id);
        if (sprintComposition.isPresent()) {
            delete(sprintComposition.get());
        }
    }

    @Override
    //@PreAuthorize("hasPermission(#sprint, 'DELETE')")
    @Transactional()
    public void delete(@Param("sprint")SprintComposition sprintComposition) {
        sprintCompositionRepository.delete(sprintComposition);
    }


}
