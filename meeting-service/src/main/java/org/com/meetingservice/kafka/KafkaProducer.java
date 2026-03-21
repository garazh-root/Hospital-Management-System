package org.com.meetingservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.com.meetingservice.events.MeetingBookedEvent;
import org.com.meetingservice.events.MeetingCancelledEvent;
import org.com.meetingservice.events.MeetingCompletedEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    private final static String TOPIC = "meeting-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(@Qualifier("meetingKafkaTemplate") KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendBookMeeting(MeetingBookedEvent event) {
        log.info("Sending MeetingBookedEvent {}", event);
        kafkaTemplate.send(TOPIC, event);
    }

    public void sendCancelMeeting(MeetingCancelledEvent event) {
        log.info("Sending MeetingCanceledEvent {}", event);
        kafkaTemplate.send(TOPIC, event);
    }

    public void sendCompletedEvent(MeetingCompletedEvent event) {
        log.info("Sending MeetingCompletedEvent {}", event);
        kafkaTemplate.send(TOPIC, event);
    }
}