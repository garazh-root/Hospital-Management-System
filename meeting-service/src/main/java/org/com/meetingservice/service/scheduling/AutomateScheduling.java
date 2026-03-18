package org.com.meetingservice.service.scheduling;

import org.com.meetingservice.service.MeetingService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutomateScheduling {

    private final MeetingService meetingService;

    public AutomateScheduling(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @Scheduled(fixedRate = 60000)
    public void automateScheduling () {
        meetingService.autoCompleteMeeting();
    }
}