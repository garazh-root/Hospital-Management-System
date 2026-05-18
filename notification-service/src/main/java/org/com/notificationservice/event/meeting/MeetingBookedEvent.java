package org.com.notificationservice.event.meeting;

import org.com.notificationservice.additional.MeetingStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public record MeetingBookedEvent(
        String meetingId,
        UUID doctorId,
        String doctorEmail,
        UUID patientId,
        String patientEmail,
        Instant dateTime,
        int durationOfMinutes,
        MeetingStatus meetingStatus,
        Instant occurredAt
) {
}
