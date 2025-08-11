package ru.otus.hw.services;

import ru.otus.hw.models.InfoSystem;
import ru.otus.hw.models.TaskMessage;

public interface WebHookService {

    void updateInfoSystem(InfoSystem infoSystem);

    void insertInfoSystem(InfoSystem infoSystem);

    void deleteInfoSystem(long infoSystemId);

    void sendWebHookOnChangeTask(TaskMessage taskMessage);

}
