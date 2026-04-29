package org.com.authservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.com.authservice.events.UserRegisteredEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducer {

    private final String TOPIC = "user-topic";
    private final KafkaTemplate<String, Object> userKafkaTemplate;

    public KafkaProducer(@Qualifier("userKafkaTemplate") KafkaTemplate<String, Object> userKafkaTemplate) {
        this.userKafkaTemplate = userKafkaTemplate;
    }

    public void sendRegisterEvent(UserRegisteredEvent userRegisteredEvent) {
        log.info("Sending user registered event {}", userRegisteredEvent);
        userKafkaTemplate.send(TOPIC, userRegisteredEvent);
    }
}