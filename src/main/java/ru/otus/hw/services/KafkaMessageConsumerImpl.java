package ru.otus.hw.services;

import java.math.BigDecimal;
import java.util.List;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.KafkaMessage;
import ru.otus.hw.models.Task;
import ru.otus.hw.models.TaskType;

import static java.util.Objects.isNull;

@Service()
@KafkaListener(
        topics = "${application.kafka.topic}",
        containerFactory = "listenerContainerFactory"
)
@RequiredArgsConstructor
public class KafkaMessageConsumerImpl implements KafkaMessageConsumer {

    private final TaskTypeService taskTypeService;

    private final InfoSystemService infoSystemService;

    private final TaskService taskService;

    private final AuthenticationConfiguration authenticationConfiguration;

    private final CustomUserDetailsService customUserDetailsService;

    private TaskType taskType;


    public void setAuthentication() throws Exception {
        SecurityContext sc = SecurityContextHolder.getContext();
        Authentication auth = sc.getAuthentication();
        if (!isNull(auth)) {
            return;
        }

        AuthenticationManager authenticationManager = authenticationConfiguration.getAuthenticationManager();
        //List<GrantedAuthority> roles = new ArrayList<>();
        //roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        var userSystem = customUserDetailsService.loadUserByUsername("SYSTEM");
        auth = new UsernamePasswordAuthenticationToken(userSystem, "", userSystem.getAuthorities());

        sc.setAuthentication(auth);

    }

    @PostConstruct
    public void init() {
        this.taskType = taskTypeService.findByName("Ошибка").get();
    }


    @KafkaHandler
    public void listen(@Payload List<KafkaMessage> messages) throws Exception {
        setAuthentication();


        for (var message: messages) {
            var oInfoSystem = infoSystemService.findByName(message.infoSystemName());
            if (oInfoSystem.isEmpty()) {
                oInfoSystem = infoSystemService.findByName("Unkown");
            }
            var infoSystem = oInfoSystem.get();

            Task task = new Task();
            task.setName(message.value());
            task.setDescription(message.value());
            task.setInfoSystem(oInfoSystem.get());
            task.setPriority(BigDecimal.valueOf(1.00));
            task.setTaskType(taskType);
            task.setProduct(infoSystem.getPrimaryProduct());
            var savedTask = taskService.save(task, true);
        }
        //log.info("messages, messages.size:{}", messages.size());

    }
    /*
    @Override
    public void accept(List<KafkaMessage> values) {
        for (var value : values) {
            log.info("log:{}", value);
        }
    }

     */
}
