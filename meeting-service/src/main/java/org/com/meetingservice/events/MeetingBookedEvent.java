package org.com.meetingservice.events;

import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.additional.Roles;

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
