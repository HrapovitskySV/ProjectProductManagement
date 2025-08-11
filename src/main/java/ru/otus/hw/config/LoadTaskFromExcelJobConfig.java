package ru.otus.hw.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.extensions.excel.poi.PoiItemReader;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.lang.NonNull;
import org.springframework.transaction.PlatformTransactionManager;
import ru.otus.hw.converters.TaskRowMapper;
import ru.otus.hw.models.Task;
import ru.otus.hw.processors.TaskItemProcessor;


@SuppressWarnings("unused")
@Configuration
@RequiredArgsConstructor
public class LoadTaskFromExcelJobConfig {
    public static final String IMPORT_TASK_JOB_NAME = "LoadTaskFromExcelJob";

    private static final int CHUNK_SIZE = 50;

    private final Logger logger = LoggerFactory.getLogger("Batch");


    @PersistenceContext
    private final EntityManager em;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private final TaskRowMapper taskRowMapper;

    @StepScope
    @Bean
    public PoiItemReader<Task> taskExcelReader(@Value("#{jobParameters['fileName']}") String fileName) {

        PoiItemReader<Task> reader = new PoiItemReader<>();
        reader.setLinesToSkip(1);  // Header rows are skipped
        reader.setResource(new FileSystemResource(fileName));
        reader.setRowMapper(taskRowMapper);
        return reader;
    }

    @Bean
    public Step stepLoadExcel(PoiItemReader<Task> taskExcelReader) {

        return new StepBuilder("excelFileToStep1", jobRepository)
                .<Task, Task>chunk(1, platformTransactionManager)
                .reader(taskExcelReader)
                .processor(processorTask())
                .writer(itemWriterTask())
                .listener(getChunkListener("задачи"))
                //.taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    @StepScope
    @Bean
    public ItemProcessor<Task, Task> processorTask() {
        return new TaskItemProcessor();
    }


    @StepScope
    @Bean
    public JpaItemWriter<Task> itemWriterTask() {
        return new JpaItemWriterBuilder<Task>()
                .entityManagerFactory(em.getEntityManagerFactory())
                .usePersist(true)
                .build();
    }

    public ChunkListener getChunkListener(String objectName) {
        return new ChunkListener() {
            public void afterChunk(@NonNull ChunkContext chunkContext) {
                var stepExecution = chunkContext.getStepContext().getStepExecution();
                logger.info("Конец пачки " + objectName + "." +
                        " Read count: " + stepExecution.getReadCount() + "" +
                        " write count: " + stepExecution.getWriteCount());
            }

            public void afterChunkError(@NonNull ChunkContext chunkContext) {
                logger.info("Ошибка пачки " + objectName);
            }
        };
    }

    @Bean
    public Job imporTaskJob(Step stepLoadExcel, Step checkUpStep) {
        return new JobBuilder(IMPORT_TASK_JOB_NAME, jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(stepLoadExcel)
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(@NonNull JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(@NonNull JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }




    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor("spring_batch");
    }
}
