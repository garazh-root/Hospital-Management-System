package org.com.meetingservice.service.filter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record SlotFilterContext(
        LocalDate date,
        List<Instant> bookedMeetings
) {
}
