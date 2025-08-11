package ru.otus.hw.services;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.AbstractInfoSystemMapper;
import ru.otus.hw.models.InfoSystem;
import ru.otus.hw.models.dto.InfoSystemDto;
import ru.otus.hw.repositories.InfoSystemRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InfoSystemServiceImpl implements InfoSystemService {

    private final InfoSystemRepository infoSystemRepository;

    private final AbstractInfoSystemMapper infoSystemMapper;

    private final WebHookService poolWebHookRestTemplateService;

    @Override
    @Cacheable("InfoSystem")
    public Optional<InfoSystem> findById(long id) {
        return infoSystemRepository.findById(id);
    }

    @Override
    public Optional<InfoSystemDto> findByIdDto(long id) {
        return infoSystemRepository.findById(id).map(infoSystemMapper::toDto);
    }

    @Override
    @Cacheable("InfoSystem")
    public Optional<InfoSystem> findByName(String name) {
        return infoSystemRepository.findByName(name);
    }

    @Override
    @Cacheable("InfoSystem")
    public List<InfoSystem> findAll() {
        return infoSystemRepository.findAll();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "InfoSystem", allEntries = true)
    public InfoSystem insert(InfoSystemDto infoSystemDto) {
        InfoSystem infoSystem = infoSystemMapper.toModel(infoSystemDto);
        infoSystem.setId(0);
        var savedInfoSystem =  infoSystemRepository.save(infoSystem);
        poolWebHookRestTemplateService.insertInfoSystem(savedInfoSystem);
        return savedInfoSystem;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "InfoSystem", allEntries = true)
    public InfoSystem update(InfoSystemDto infoSystemDto) {
        InfoSystem infoSystem = infoSystemMapper.toModel(infoSystemDto);
        var savedInfoSystem =  infoSystemRepository.save(infoSystem);
        poolWebHookRestTemplateService.updateInfoSystem(savedInfoSystem);
        return savedInfoSystem;
    }


    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = "InfoSystem", allEntries = true)
    public void deleteById(long id) {
        infoSystemRepository.deleteById(id);
        poolWebHookRestTemplateService.deleteInfoSystem(id);
    }



    @Nonnull
    @Override
    public List<InfoSystem> findByUseWebHook(boolean useWebHook) {
        return infoSystemRepository.findAllByUseWebHook(useWebHook);
    }

}
