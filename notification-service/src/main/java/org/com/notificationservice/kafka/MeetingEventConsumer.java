package org.com.notificationservice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.com.notificationservice.event.meeting.MeetingBookedEvent;
import org.com.notificationservice.event.meeting.MeetingCancelledEvent;
import org.com.notificationservice.event.meeting.MeetingCompletedEvent;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@KafkaListener(topics = "meeting-topic", groupId = "notification")
public class MeetingEventConsumer {

    @KafkaHandler
    public void onMeetingBooked(MeetingBookedEvent event) {
        log.info("Meeting booked | meetingId : {} | doctorId : {} | patientId : {} | dateTime : {} | duration : {} | status : {} | occurredAt : {}",
                event.meetingId(),
                event.doctorId(),
                event.patientId(),
                event.dateTime(),
                event.durationOfMinutes(),
                event.meetingStatus(),
                event.occurredAt());
    }

    @KafkaHandler
    public void onMeetingCancelled(MeetingCancelledEvent event) {
        log.info("Meeting cancelled | meetingId : {} | patientId : {} | doctorId : {} | occurredAt : {}",
                event.meetingId(),
                event.patientId(),
                event.doctorId(),
                event.occurredAt());
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