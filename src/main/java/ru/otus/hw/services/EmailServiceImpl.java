package ru.otus.hw.services;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import ru.otus.hw.converters.AbstractTaskMapper;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender emailSender;

    private final AbstractTaskMapper abstractTaskMapper;

    public void sendMessageOnTask(Map<String, String> messageMap) throws MessagingException {

            SimpleMailMessage message = new SimpleMailMessage();

            String from = (String) ((JavaMailSenderImpl) emailSender).getJavaMailProperties().get("mail.from");

            //MimeMessage message = emailSender.createMimeMessage();
            //MimeMessageHelper helper = new MimeMessageHelper(message, true);
            message.setFrom(from);
            message.setTo(messageMap.get("to"));
            message.setSubject(messageMap.get("title"));
            message.setText(messageMap.get("text"));

            emailSender.send(message);
    }
}
