package org.com.notificationservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.com.notificationservice.event.patient.PatientCreatedEvent;
import org.com.notificationservice.event.patient.PatientStatusUpdatedEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@KafkaListener(topics = "patient-topic", groupId = "notification")
public class PatientEventConsumer {

    @KafkaHandler
    public void onPatientCreated(PatientCreatedEvent event) {
        log.info("Patient Created | id : {} | firstName: {} | lastName:  {} | email:  {} | phoneNumber: {} | status:  {}",
                event.id(),
                event.firstName(),
                event.lastName(),
                event.email(),
                event.phoneNumber(),
                event.status());
    }

    @KafkaHandler
    public void onPatientStatusUpdated(PatientStatusUpdatedEvent event) {
        log.info("Patient status updated | id : {} | oldStatus : {} | newStatus : {}",
                event.patientId(),
                event.oldStatus(),
                event.newStatus());
    }
}