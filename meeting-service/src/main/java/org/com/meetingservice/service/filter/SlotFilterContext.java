package org.com.meetingservice.service.filter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record SlotFilterContext(
        LocalDate date,
        List<LocalDateTime> bookedMeetings
) {
}
