package org.com.notificationservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.com.notificationservice.event.doctor.DoctorChangedStatusEvent;
import org.com.notificationservice.event.doctor.DoctorCreatedEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@KafkaListener(topics = "doctor-topic", groupId = "notification")
public class DoctorEventConsumer {

    @KafkaHandler
    public void onDoctorCreated(DoctorCreatedEvent event) {
        log.info("Doctor Created Event | id : {} | firstName : {} | lastName : {} | email : {} | phoneNumber : {} | status : {} ",
                event.id(),
                event.firstName(),
                event.lastName(),
                event.email(),
                event.phoneNumber(),
                event.status());
    }

    @KafkaHandler
    public void onDoctorUpdatedStatus(DoctorChangedStatusEvent event) {
        log.info("Doctor status updated | id : {} | oldStatus : {} | newStatus : {}",
                event.id(),
                event.oldStatus(),
                event.newStatus());
    }
}