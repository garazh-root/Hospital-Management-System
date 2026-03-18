package org.com.patientservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.com.patientservice.dto.PatientResponseDTO;
import org.com.patientservice.events.PatientCreatedEvent;
import org.com.patientservice.events.PatientStatusUpdatedEvent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducer {

    private final static String TOPIC = "patient-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(@Qualifier("patientKafkaTemplate") KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPatientCreated(PatientCreatedEvent patientCreatedEvent) {
        log.info("Patient Created: {}", patientCreatedEvent);
        kafkaTemplate.send(TOPIC, patientCreatedEvent);
    }

    public void sendPatientStatusUpdated(PatientStatusUpdatedEvent patientStatusUpdatedEvent) {
        log.info("Patient Status Updated: {}", patientStatusUpdatedEvent);
        kafkaTemplate.send(TOPIC, patientStatusUpdatedEvent);
    }

}