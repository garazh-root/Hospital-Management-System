package org.com.doctorservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.com.doctorservice.events.DoctorDeletedEvent;
import org.com.doctorservice.dto.DoctorResponseDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class KafkaProducer {

    private static final String TOPIC = "doctor-topic";
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(@Qualifier("doctorKafkaTemplate") KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendDoctorCreated(DoctorResponseDTO doctorResponseDTO) {
        log.info("Doctor created: {}", doctorResponseDTO);
        kafkaTemplate.send(TOPIC, doctorResponseDTO);
    }

    public void sendDoctorUpdated(DoctorResponseDTO doctorResponseDTO) {
        log.info("Doctor updated: {}", doctorResponseDTO);
        kafkaTemplate.send(TOPIC, doctorResponseDTO);
    }

    public void sendDoctorDeleted(UUID doctorId) {
        DoctorDeletedEvent doctorDeletedEvent = new DoctorDeletedEvent(doctorId);
        log.info("Doctor deleted: {}", doctorDeletedEvent);
        kafkaTemplate.send(TOPIC, doctorDeletedEvent);
    }
}