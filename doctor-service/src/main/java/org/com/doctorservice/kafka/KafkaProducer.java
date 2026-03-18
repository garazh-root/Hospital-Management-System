package org.com.doctorservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.com.doctorservice.events.DoctorChangedStatusEvent;
import org.com.doctorservice.events.DoctorCreatedEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    private final static String TOPIC = "doctor-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(@Qualifier("doctorKafkaTemplate")KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendDoctorCreated(DoctorCreatedEvent doctorCreatedEvent) {
        log.info("Doctor Created: {}", doctorCreatedEvent);
        kafkaTemplate.send(TOPIC, doctorCreatedEvent);
    }

    public void sendPatientStatusUpdated(DoctorChangedStatusEvent doctorCreatedEvent) {
        log.info("Patient Status Updated: {}", doctorCreatedEvent);
        kafkaTemplate.send(TOPIC, doctorCreatedEvent);
    }
}