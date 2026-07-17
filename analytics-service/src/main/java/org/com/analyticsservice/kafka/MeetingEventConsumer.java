package org.com.analyticsservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.analyticsservice.events.MeetingBookedEvent;
import org.com.analyticsservice.events.MeetingCancelledEvent;
import org.com.analyticsservice.events.MeetingCompletedEvent;
import org.com.analyticsservice.service.AnalyticsService;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@KafkaListener(topics = "meeting-topic", groupId = "analytics")
public class MeetingEventConsumer {

    private final AnalyticsService analyticsService;

    @KafkaHandler
    public void onMeetingBooked(MeetingBookedEvent event) {
        log.info("Received MeetingBooked event {}", event);
        analyticsService.handleMeetingBooked(event.doctorId(), event.dateTime());
    }

    @KafkaHandler
    public void onMeetingCompleted(MeetingCompletedEvent event) {
        log.info("Received MeetingCompleted event {}", event);
        analyticsService.handleMeetingCompleted(event.doctorId(), event.occurredAt());
    }

    @KafkaHandler
    public void onMeetingCancelled(MeetingCancelledEvent event) {
        log.info("Received MeetingCancelled event {}", event);
        analyticsService.handleMeetingCancelled(event.doctorId(), event.dateTime());
    }
}