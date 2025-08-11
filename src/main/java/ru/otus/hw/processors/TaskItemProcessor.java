package ru.otus.hw.processors;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.models.Task;

@RequiredArgsConstructor
public class TaskItemProcessor implements ItemProcessor<Task, Task> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskItemProcessor.class);


    @Override
    public Task process(final Task item) {
        //var author = new Author(0,authorMongo.getFullName());
        //mapObjectService.putAuthor(authorMongo.getId(), author);
        LOGGER.info("Processing task information: {}", item);
        return item;
    }
}