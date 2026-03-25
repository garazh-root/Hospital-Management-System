package org.com.meetingservice.service.filter;

import org.com.meetingservice.dto.AvailableSlotResponse;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.ZoneOffset.UTC;

@Component
public class BookedSlotStrategy implements SlotStrategy {

    @Override
    public List<AvailableSlotResponse> filter(List<AvailableSlotResponse> slots, SlotFilterContext context) {
        Set<LocalTime> timeSet = context.bookedMeetings()
                .stream()
                .filter(m -> m.atZone(UTC).toLocalDate().equals(context.date()))
                .map(m -> m.atZone(UTC).toLocalTime())
                .collect(Collectors.toSet());

        return slots.stream()
                .filter(slot -> !timeSet.contains(LocalTime.parse(slot.startTime())))
                .collect(Collectors.toList());
    }
}