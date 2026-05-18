package org.com.meetingservice.events;

import java.time.Instant;
import java.util.UUID;

public record MeetingCompletedEvent(
        String meetingId,
        UUID patientId,
        String patientEmail,
        UUID doctorId,
        String doctorEmail,
        Instant occurredAt
) {
}
