package org.com.doctorservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.doctorservice.events.MeetingRatedEvent;
import org.com.doctorservice.exception.DoctorNotFoundException;
import org.com.doctorservice.messages.DoctorServiceMessages;
import org.com.doctorservice.model.Doctor;
import org.com.doctorservice.repository.DoctorRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final DoctorRepository doctorRepository;

    @KafkaListener(topics = "rating-topic", groupId = "doctor")
    public void handle(MeetingRatedEvent meetingRatedEvent) {
        Doctor doctor = doctorRepository.findById(meetingRatedEvent.doctorId())
                .orElseThrow(() ->  new DoctorNotFoundException(DoctorServiceMessages.DOCTOR_NOT_FOUND.getMessage()));

        BigDecimal rating = doctor.getRating();
        int totalRating = doctor.getTotalRating();

        BigDecimal newAverage = rating
                .multiply(BigDecimal.valueOf(totalRating))
                .add(BigDecimal.valueOf(meetingRatedEvent.rating()))
                .divide(BigDecimal.valueOf(totalRating + 1), 1, RoundingMode.HALF_UP);

        doctor.setRating(newAverage);
        doctor.setTotalRating(totalRating + 1);

        doctorRepository.save(doctor);
    }
}