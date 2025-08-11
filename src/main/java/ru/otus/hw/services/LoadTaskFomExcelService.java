package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Service;

import java.util.Properties;

import static java.time.LocalTime.now;
import static ru.otus.hw.config.LoadTaskFromExcelJobConfig.IMPORT_TASK_JOB_NAME;

@RequiredArgsConstructor
@Service
public class LoadTaskFomExcelService {

    private final JobOperator jobOperator;

    public void starLoadTaskJobWithJob(String uploadFileName) throws Exception {
        Properties properties = new Properties();
        properties.put("key", Integer.toString(now().getNano()));
        properties.put("fileName",uploadFileName);

        Long executionId = jobOperator.start(IMPORT_TASK_JOB_NAME, properties);
        System.out.println(jobOperator.getSummary(executionId));
    }
}
