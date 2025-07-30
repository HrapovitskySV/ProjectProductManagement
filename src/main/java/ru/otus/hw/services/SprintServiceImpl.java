package ru.otus.hw.services;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.converters.AbstractSprintMapper;
import ru.otus.hw.models.Sprint;
import ru.otus.hw.models.dto.SprintDto;
import ru.otus.hw.models.dto.SprintDtoInputWeb;
import ru.otus.hw.repositories.SprintRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SprintServiceImpl implements SprintService {

    private final SprintRepository sprintRepository;

    private final AbstractSprintMapper sprintMapper;


    @Override
    @PostAuthorize("returnObject.isPresent() ? hasPermission(returnObject.get(), 'READ') : true")
    @Transactional(readOnly = true)
    public Optional<Sprint> findById(long id) {
        var sprint = sprintRepository.findById(id);
        return sprint;
    }

    @Override
    @PostAuthorize("returnObject.isPresent() ? hasPermission(returnObject.get(), 'READ') : true")
    @Transactional(readOnly = true)
    public Optional<SprintDto> findByIdToDto(long id) {
        var sprint = sprintRepository.findById(id).map(sprintMapper::toDto);
        return sprint;
    }


    @Override
    @PostFilter("hasPermission(filterObject, 'READ')")
    @Transactional(readOnly = true)
    public List<SprintDto> findAll() {
        var sprints = sprintRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<SprintDto> sprintsDto = new ArrayList<>();
        for (var sprint: sprints) {
            sprintsDto.add(sprintMapper.toDto(sprint));
        }
        //var sprintsDto = sprintRepository.findAll().stream().map(sprintMapper::toDto).toList(); //почему-то возвращает иммутабельный лист и фильтр не может его отфильтровать

        return sprintsDto;
    }


    @Override
    @Transactional()
    @PreAuthorize("hasPermission(#sprintDto, 'READ')")
    public Sprint insert(@Param("sprintDto") SprintDtoInputWeb sprintDto) {
        return save(sprintDto, true);
    }


    @Override
    @Transactional()
    @PreAuthorize("hasPermission(#sprintDto, 'READ')")
    public Sprint update(@Param("sprintDto") SprintDtoInputWeb sprintDto) {
        return save(sprintDto, false);
    }

    @Override
    @Transactional()
    public void deleteById(long id) {
        Optional<Sprint> sprint = sprintRepository.findById(id);
        if (sprint.isPresent()) {
            delete(sprint.get());
        }
    }

    @Override
    @PreAuthorize("hasPermission(#sprint, 'DELETE')")
    @Transactional()
    public void delete(@Param("sprint")Sprint sprint) {
        sprintRepository.delete(sprint);
    }

    @Override
    @PreAuthorize("hasPermission(#sprint, 'WRITE')")
    public Sprint save(@Param("sprint")Sprint sprint, boolean isNewSprint) {
        if (isNewSprint) {
            sprint.setId(0);
        }

        var savedSprint = sprintRepository.save(sprint);


        return savedSprint;
    }

    private Sprint save(SprintDtoInputWeb sprintDto, boolean isNewSprint) {
        Sprint sprint = sprintMapper.toModel(sprintDto);

        return save(sprint, isNewSprint);
    }
}
