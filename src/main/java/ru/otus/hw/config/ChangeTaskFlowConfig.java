package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.messaging.PollableChannel;
import org.springframework.integration.channel.QueueChannel;
import ru.otus.hw.converters.AbstractTaskMapper;
import ru.otus.hw.models.InfoSystem;
import ru.otus.hw.models.TaskMessage;
import ru.otus.hw.services.EmailService;
import ru.otus.hw.services.WebHookService;

import java.util.HashMap;

import static java.util.Objects.isNull;

@Configuration
public class ChangeTaskFlowConfig {

    @MessagingGateway
    public interface TaskFlowGateway {
        @Gateway(requestChannel = "changeTaskFlow.input")
        void changeTask(TaskMessage taskMessage);

    }

    @Bean
    public PollableChannel emailChannel() {
        return new QueueChannel(25);
    }

    @Bean
    public PollableChannel webHookChannel() {
        return new QueueChannel(25);
    }


    @Bean
    public IntegrationFlow changeTaskFlow() {
        return flow -> flow
                .routeToRecipients(r -> r
                        .applySequence(true)
                        .ignoreSendFailures(true)
                        .recipient(emailChannel())
                        .recipient(webHookChannel())
                );
    }

    @Bean
    public IntegrationFlow emailsFlow(AbstractTaskMapper taskMapper,EmailService emailService) {
        return IntegrationFlow.from(emailChannel())
                .<TaskMessage>filter((taskMessage) -> {
                    var responsible = taskMessage.getTask().getResponsible();

                    return (!isNull(responsible) &&
                            responsible.isInformAboutTasks() &&
                            !(responsible.getEmail().isEmpty()));
                })
                .transform((o) -> {
                            var taskMessage = (TaskMessage) o;
                            var rez = new HashMap<String, String>();
                            var task = taskMessage.getTask();
                            rez.put("text", taskMapper.getViewTask(task));
                            if (taskMessage.isNewTask()) {
                                rez.put("title", "New Task: " + task.getId() + " " + task.getName());
                            } else {
                                rez.put("title", "Changes Task: " + task.getId() + " " + task.getName());
                            }
                            rez.put("to", task.getResponsible().getEmail());
                            return rez;
                        }
                )
                .handle(emailService, "sendMessageOnTask")
                .get();
    }

    @Bean
    public IntegrationFlow  webHookFlow(WebHookService poolWebHookRestTemplateService) {
        return IntegrationFlow.from(webHookChannel())
                .<TaskMessage>filter((taskMessage) -> {
                    InfoSystem infoSystem = taskMessage.getTask().getInfoSystem();
                    return (!isNull(infoSystem) &&
                            infoSystem.getUseWebHook() &&
                            !(infoSystem.getUrlWebHook().isEmpty()));
                })
                .handle(poolWebHookRestTemplateService, "sendWebHookOnChangeTask")
                .get();
    }

}
