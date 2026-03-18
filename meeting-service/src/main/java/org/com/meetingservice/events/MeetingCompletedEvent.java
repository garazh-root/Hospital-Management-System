package org.com.meetingservice.events;

import java.time.Instant;
import java.util.UUID;

public record MeetingCompletedEvent(
        String meetingId,
        UUID patientId,
        UUID doctorId,
        Instant occurredAt
) {
}
