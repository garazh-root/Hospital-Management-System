package org.com.meetingservice.service.scheduling;

import lombok.extern.slf4j.Slf4j;
import org.com.meetingservice.service.MeetingService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@EnableScheduling
@Slf4j
public class AutomateScheduling {

    private final MeetingService meetingService;

    public AutomateScheduling(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @Scheduled(fixedRate = 60000)
    public void automateScheduling () {
        log.info("Automate Scheduling enabled at {}", Instant.now());
        meetingService.autoCompleteMeeting();
    }
}