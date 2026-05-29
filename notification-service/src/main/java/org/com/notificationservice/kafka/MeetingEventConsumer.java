package org.com.notificationservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.notificationservice.additional.Roles;
import org.com.notificationservice.event.meeting.MeetingBookedEvent;
import org.com.notificationservice.event.meeting.MeetingCancelledEvent;
import org.com.notificationservice.event.meeting.MeetingCompletedEvent;
import org.com.notificationservice.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@KafkaListener(topics = "meeting-topic", groupId = "notification")
@RequiredArgsConstructor
public class MeetingEventConsumer {

    private final EmailService emailService;

    @Value("{MAIL.USERNAME}")
    private String username;

    @KafkaHandler
    public void onMeetingBooked(MeetingBookedEvent event) {
        emailService.sendBookingEmail(event.patientEmail(), username, event.doctorFirstName(), event.doctorLastName(), Roles.PATIENT, event.dateTime(), event.durationOfMinutes());
        emailService.sendBookingEmail(event.doctorEmail(), username, event.doctorFirstName(), event.doctorLastName(), Roles.DOCTOR, event.dateTime(), event.durationOfMinutes());
        log.info("Meeting Booked Successfully for date/time {}",  event.dateTime());
    }

    @KafkaHandler
    public void onMeetingCancelled(MeetingCancelledEvent event) {
        emailService.sendBookingCancelledEmail(event.patientEmail(), username, event.dateTime());
        emailService.sendBookingCancelledEmail(event.doctorEmail(), username, event.dateTime());
        log.info("Meeting Cancelled for date/time {}",  event.dateTime());
    }

    @KafkaHandler
    public void onMeetingCompleted(MeetingCompletedEvent event) {
        log.info("Meeting completed | meetingId : {} | patientId : {} | doctorId : {} | occurredAt : {}",
                event.meetingId(),
                event.patientId(),
                event.doctorId(),
                event.occurredAt());
    }
}