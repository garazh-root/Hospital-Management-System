package org.com.notificationservice.event.meeting;

import java.time.Instant;
import java.util.UUID;

public record MeetingCancelledEvent(
        String meetingId,
        UUID patientId,
        UUID doctorId,
        Instant occurredAt
) {
}
