package org.com.meetingservice.events;

import org.com.meetingservice.additional.MeetingStatus;

import java.time.Instant;
import java.util.UUID;

public record MeetingCanceledEvent(
        String meetingId,
        UUID patientId,
        UUID doctorId,
        Instant occurredAt
) {
}
