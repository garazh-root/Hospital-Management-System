package org.com.meetingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.com.meetingservice.dto.*;
import org.com.meetingservice.service.factory.SlotFactory;
import org.com.meetingservice.service.filter.SlotFilterContext;
import org.com.meetingservice.service.filter.SlotStrategy;
import org.com.meetingservice.service.resolver.ScheduleResolver;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class SlotGeneratorService {

    private SlotFactory slotFactory;
    private ScheduleResolver scheduleResolver;
    private List<SlotStrategy> slotStrategies;

    public SlotGeneratorService(SlotFactory slotFactory, ScheduleResolver scheduleResolver, List<SlotStrategy> slotStrategies) {
        this.slotFactory = slotFactory;
        this.scheduleResolver = scheduleResolver;
        this.slotStrategies = slotStrategies;
    }

    public List<AvailableSlotResponse> generateAvailableSlots(
            ScheduleResponse scheduleResponse,
            LocalDate date,
            List<Instant> bookedMeetings
    ) {
        ResolvedSchedule schedule = scheduleResolver.resolve(scheduleResponse, date);

        if(schedule.unavailable()){
            return Collections.emptyList();
        }

        List<AvailableSlotResponse> availableSlots = slotFactory.generateSlots(date, schedule);

        SlotFilterContext slotFilterContext = new SlotFilterContext(date, bookedMeetings);

        for(SlotStrategy slotFilter : slotStrategies){
            availableSlots = slotFilter.filter(availableSlots, slotFilterContext);
        }

        return availableSlots;
    }
}