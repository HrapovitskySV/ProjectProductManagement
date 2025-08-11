package ru.otus.hw.services;

import java.util.List;
import ru.otus.hw.models.KafkaMessage;

public interface KafkaMessageConsumer {

    void listen(List<KafkaMessage> value) throws Exception;
}
