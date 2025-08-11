package ru.otus.hw.services;

import jakarta.annotation.Nonnull;
import ru.otus.hw.models.InfoSystem;
import ru.otus.hw.models.dto.InfoSystemDto;

import java.util.List;
import java.util.Optional;

public interface InfoSystemService {

    Optional<InfoSystem> findById(long id);

    Optional<InfoSystemDto> findByIdDto(long id);

    Optional<InfoSystem> findByName(String name);

    List<InfoSystem> findAll();

    InfoSystem insert(InfoSystemDto infoSystemDto);

    InfoSystem update(InfoSystemDto infoSystemDto);

    void deleteById(long id);

    @Nonnull
    List<InfoSystem> findByUseWebHook(boolean useWebHook);

}
