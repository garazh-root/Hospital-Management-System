package org.com.notificationservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.notificationservice.event.user.UserRegisteredEvent;
import org.com.notificationservice.service.EmailService;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "user-topic", groupId = "notification")
@RequiredArgsConstructor
@Slf4j
public class UserEmailConsumer {

    private final EmailService emailService;

    @KafkaHandler
    public void handle(UserRegisteredEvent event) {
        emailService.sendWelcomeEmail(
                event.email(), event.username(), event.role());

    }
}