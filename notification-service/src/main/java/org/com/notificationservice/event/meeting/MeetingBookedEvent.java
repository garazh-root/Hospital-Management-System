package org.com.notificationservice.event.meeting;

import org.com.notificationservice.additional.MeetingStatus;
import org.com.notificationservice.additional.Roles;

import java.time.Instant;
import java.time.LocalDateTime;
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
