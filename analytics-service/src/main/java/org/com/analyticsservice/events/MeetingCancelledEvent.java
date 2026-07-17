package org.com.analyticsservice.events;

import java.time.Instant;
import java.util.UUID;

public record MeetingCancelledEvent(
        String meetingId,
        UUID patientId,
        String patientEmail,
        UUID doctorId,
        String doctorEmail,
        Instant dateTime
) {
}
