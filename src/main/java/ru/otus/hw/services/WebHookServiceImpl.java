package ru.otus.hw.services;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.otus.hw.models.InfoSystem;
import ru.otus.hw.models.TaskMessage;

import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;

import static java.util.Objects.isNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service

public class WebHookServiceImpl implements WebHookService {

    private Logger logger = LoggerFactory.getLogger(WebHookServiceImpl.class);

    private final Map<Long, RestClient> mapRestClient = new ConcurrentHashMap<>();

    public void updateInfoSystem(@NotNull InfoSystem infoSystem) {
        if (infoSystem.getUseWebHook()) {
            mapRestClient.put(infoSystem.getId(),
                    RestClient.builder().baseUrl(infoSystem.getUrlWebHook()).build());
        } else {
            mapRestClient.remove(infoSystem.getId());
        }
    }

    public void insertInfoSystem(@NotNull InfoSystem infoSystem) {

        if (infoSystem.getUseWebHook()) {
            mapRestClient.put(infoSystem.getId(),
                    RestClient.builder().baseUrl(infoSystem.getUrlWebHook()).build());

        }
    }

    public void deleteInfoSystem(long infoSystemId) {

        mapRestClient.remove(infoSystemId);

    }

    private RestClient getRestClientOnInsertTask(InfoSystem infoSystem) {

        if (isNull(infoSystem)) {
            return null;
        }

        RestClient restClient = null;

        if (infoSystem.getUseWebHook()) {
            restClient = mapRestClient.get(infoSystem.getId());
            if (isNull(restClient)) {
                insertInfoSystem(infoSystem);
                restClient = mapRestClient.get(infoSystem.getId());
            }
        }

        return restClient;
    }

    public void  sendWebHookOnChangeTask(TaskMessage taskMessage) {
        var task = taskMessage.getTask();

        RestClient restClient = getRestClientOnInsertTask(task.getInfoSystem());

        if (!isNull(restClient)) {
            if (taskMessage.isNewTask()) {
                ResponseEntity<Void> response = restClient.post()
                        .uri("")
                        .contentType(APPLICATION_JSON)
                        .body(task)
                        .retrieve()
                        .toBodilessEntity();
            } else if (taskMessage.isDeleteTask()) {
                ResponseEntity<Void> response = restClient.delete()
                        .uri("/" + task.getId())
                        .retrieve()
                        .toBodilessEntity();

            } else {
                ResponseEntity<Void> response = restClient.put()
                        .uri("/" + task.getId())
                        .contentType(APPLICATION_JSON)
                        .body(task)
                        .retrieve()
                        .toBodilessEntity();
            }
        }
    }
}
