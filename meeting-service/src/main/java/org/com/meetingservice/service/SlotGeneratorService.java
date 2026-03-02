package org.com.meetingservice.service;

import lombok.extern.slf4j.Slf4j;
import org.com.meetingservice.dto.AvailableSlotResponse;
import org.com.meetingservice.dto.ScheduleResponse;
import org.com.meetingservice.dto.ScheduleOverrideDTO;
import org.com.meetingservice.dto.ScheduleTemplateDTO;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SlotGeneratorService {

    public List<AvailableSlotResponse> generateAvailableSlots(
            ScheduleResponse doctorScheduleDataResponse,
            LocalDate date,
            List<LocalDateTime> bookedMeetings
    ) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        Optional<ScheduleTemplateDTO> plainScheduleTemplate = doctorScheduleDataResponse.listOfScheduleTemplateResponse()
                .stream()
                .filter(t -> t.dayOfTheWeek().equals(dayOfWeek.name()))
                .findFirst();

        if (plainScheduleTemplate.isEmpty()) {
            log.info("No template found for {} on {}", dayOfWeek, date);
        }

        ScheduleTemplateDTO scheduleTemplateDTO = plainScheduleTemplate.get();

        Optional<ScheduleOverrideDTO> plainScheduleOverride = doctorScheduleDataResponse.listOfScheduleOverrideResponse()
                .stream()
                .filter(o -> LocalDate.parse(o.date()).equals(date))
                .findFirst();

        if (plainScheduleOverride.isPresent() && "UNAVAILABLE".equals(plainScheduleOverride.get().overrideType())) {
            log.info("Doctor not available for {} on {}", dayOfWeek, date);
            return Collections.emptyList();
        }

        LocalTime startTime, endTime, breakStartTime, breakEndTime;
        int slotDuration;

        if (plainScheduleOverride.isPresent() && "CUSTOM_HOURS".equals(plainScheduleOverride.get().overrideType())) {
            ScheduleOverrideDTO overrideDTO = plainScheduleOverride.get();
            startTime = LocalTime.parse(overrideDTO.startTime());
            endTime = LocalTime.parse(overrideDTO.endTime());
            breakStartTime = null;
            breakEndTime = null;
            slotDuration = Integer.parseInt(overrideDTO.slotDurationOfMinutes());
        } else {
            startTime = LocalTime.parse(scheduleTemplateDTO.startTime());
            endTime = LocalTime.parse(scheduleTemplateDTO.endTime());
            breakStartTime = LocalTime.parse(scheduleTemplateDTO.breakStartTime());
            breakEndTime = LocalTime.parse(scheduleTemplateDTO.breakEndTime());
            slotDuration = Integer.parseInt(scheduleTemplateDTO.slotDurationOfMinutes());
        }

        List<AvailableSlotResponse> slots = new ArrayList<>();
        LocalTime currentTime = startTime;

        while (currentTime.plusMinutes(slotDuration).isBefore(endTime) ||
                currentTime.plusMinutes(slotDuration).equals(endTime)) {

            if(breakStartTime != null && breakEndTime != null) {
                if(currentTime.isBefore(breakEndTime) && currentTime.plusMinutes(slotDuration).isAfter(breakStartTime)) {
                    currentTime = breakEndTime;
                    continue;
                }
            }

            slots.add(new AvailableSlotResponse(
                    date.toString(),
                    currentTime.toString(),
                    currentTime.plusMinutes(slotDuration).toString(),
                    Integer.toString(slotDuration)
            ));

            currentTime = currentTime.plusMinutes(slotDuration);
        }

        Set<LocalTime> bookedTimers = bookedMeetings
                .stream()
                .filter(f -> f.toLocalDate().equals(date))
                .map(LocalDateTime::toLocalTime)
                .collect(Collectors.toSet());

        return slots.stream()
                .filter(slot -> !bookedTimers.contains(LocalTime.parse(slot.startTime())))
                .collect(Collectors.toList());
    }

}