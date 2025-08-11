package ru.otus.hw.services;


import jakarta.mail.MessagingException;

import java.util.Map;

public interface EmailService {
    void sendMessageOnTask(Map<String, String> messageMap) throws MessagingException;
}
