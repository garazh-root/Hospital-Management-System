package org.com.analyticsservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.analyticsservice.events.UserRegisteredEvent;
import org.com.analyticsservice.service.AnalyticsService;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "user-topic", groupId = "analytics")
public class RegistrationConsumer {

    private final AnalyticsService analyticsService;

    @KafkaHandler
    public void onUserRegistered(UserRegisteredEvent event) {
        log.info("Received UserRegistered event {}", event);
        analyticsService.handleRegistration(event.role().toString());
    }
}