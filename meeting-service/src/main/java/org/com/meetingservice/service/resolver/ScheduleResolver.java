package org.com.meetingservice.service.resolver;

import lombok.extern.slf4j.Slf4j;
import org.com.meetingservice.additional.OverrideType;
import org.com.meetingservice.dto.ResolvedSchedule;
import org.com.meetingservice.dto.ScheduleOverrideDTO;
import org.com.meetingservice.dto.ScheduleResponse;
import org.com.meetingservice.dto.ScheduleTemplateDTO;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Component
@Slf4j
public class ScheduleResolver {

    public ResolvedSchedule resolve(ScheduleResponse scheduleResponse, LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();

        Optional<ScheduleOverrideDTO> optionalScheduleOverrideDTO = findOverride(scheduleResponse, date);

        if(optionalScheduleOverrideDTO.isPresent() && OverrideType.UNAVAILABLE.equals(OverrideType.valueOf(optionalScheduleOverrideDTO.get().overrideType()))) {
            log.info("Overriding schedule override for date {}", date);
            return ResolvedSchedule.notAvailable();
        }

        if(optionalScheduleOverrideDTO.isPresent() && OverrideType.CUSTOM_HOURS.equals(OverrideType.valueOf(optionalScheduleOverrideDTO.get().overrideType()))) {
            return buildFromOverride(optionalScheduleOverrideDTO.get());
        }

        ScheduleTemplateDTO scheduleTemplateDTO = findTemplate(scheduleResponse, dayOfWeek)
                .orElseThrow(() -> new IllegalArgumentException("No schedule template found for date " + date));

        return buildFromTemplate(scheduleTemplateDTO);
    }

    private Optional<ScheduleTemplateDTO> findTemplate(ScheduleResponse scheduleResponse, DayOfWeek dayOfWeek) {
        return scheduleResponse.listOfScheduleTemplateResponse()
                .stream()
                .filter(template -> template.dayOfTheWeek().equals(dayOfWeek.name()))
                .findFirst();
    }

    private Optional<ScheduleOverrideDTO> findOverride(ScheduleResponse scheduleResponse, LocalDate date) {
        return scheduleResponse.listOfScheduleOverrideResponse()
                .stream()
                .filter(override -> LocalDate.parse(override.date()).equals(date))
                .findFirst();
    }

    private ResolvedSchedule buildFromTemplate(ScheduleTemplateDTO scheduleTemplateDTO) {
        return ResolvedSchedule.fromTemplate(
                LocalTime.parse(scheduleTemplateDTO.startTime()),
                LocalTime.parse(scheduleTemplateDTO.endTime()),
                LocalTime.parse(scheduleTemplateDTO.breakStartTime()),
                LocalTime.parse(scheduleTemplateDTO.breakEndTime()),
                Integer.parseInt(scheduleTemplateDTO.slotDurationOfMinutes())
        );
    }

    private ResolvedSchedule buildFromOverride(ScheduleOverrideDTO scheduleOverrideDTO) {
        return ResolvedSchedule.fromOverride(
                LocalTime.parse(scheduleOverrideDTO.startTime()),
                LocalTime.parse(scheduleOverrideDTO.endTime()),
                Integer.parseInt(scheduleOverrideDTO.slotDurationOfMinutes())
        );
    }
}