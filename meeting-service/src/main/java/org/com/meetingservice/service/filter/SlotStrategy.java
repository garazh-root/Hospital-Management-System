package org.com.meetingservice.service.filter;

import org.com.meetingservice.dto.AvailableSlotResponse;

import java.util.List;

public interface SlotStrategy {
    List<AvailableSlotResponse> filter(List<AvailableSlotResponse> slots, SlotFilterContext context);
}
