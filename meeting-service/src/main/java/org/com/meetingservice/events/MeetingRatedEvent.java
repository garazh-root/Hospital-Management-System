package org.com.meetingservice.events;

import java.time.Instant;
import java.util.UUID;

public record MeetingRatedEvent(
        String meetingId, UUID doctorId, UUID patientId, int rating,  String comment, Instant ratedAt
) {
}
