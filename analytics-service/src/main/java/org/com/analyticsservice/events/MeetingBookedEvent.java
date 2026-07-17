package org.com.analyticsservice.events;

import org.com.analyticsservice.addtitional.MeetingStatus;
import org.com.analyticsservice.addtitional.Roles;

import java.time.Instant;
import java.util.UUID;

public record MeetingBookedEvent(
        String meetingId,
        UUID doctorId,
        String doctorFirstName,
        String doctorLastName,
        String doctorEmail,
        UUID patientId,
        String patientEmail,
        Roles role,
        Instant dateTime,
        int durationOfMinutes,
        MeetingStatus meetingStatus,
        Instant occurredAt
) {
}
