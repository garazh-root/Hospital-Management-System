package org.com.meetingservice.service;

import org.com.meetingservice.dto.ResolvedSchedule;
import org.com.meetingservice.dto.ScheduleOverrideDTO;
import org.com.meetingservice.dto.ScheduleResponse;
import org.com.meetingservice.dto.ScheduleTemplateDTO;
import org.com.meetingservice.service.resolver.ScheduleResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScheduleResolverTest {

    private ScheduleResolver scheduleResolver;

    private static final LocalDate MONDAY = LocalDate.of(2025, 7, 7);

    @BeforeEach
    void setUp() {
        scheduleResolver = new ScheduleResolver();
    }

    @Test
    void buildFromTemplateShouldCorrectlyBuildScheduleWhenNoOverride () {
        ScheduleTemplateDTO scheduleTemplateDTO = mockScheduleTemplateDTO(
                "MONDAY", "09:00", "17:00", "12:00", "13:00", "30");
        ScheduleResponse scheduleResponse = mockScheduleResponse(List.of(scheduleTemplateDTO), Collections.emptyList());

        ResolvedSchedule schedule = scheduleResolver.resolve(scheduleResponse, MONDAY);

        assertThat(schedule.unavailable()).isFalse();
        assertThat(schedule.startTime().toString()).isEqualTo("09:00");
        assertThat(schedule.endTime().toString()).isEqualTo("17:00");
        assertThat(schedule.duration()).isEqualTo(30);
    }

    @Test
    void buildFromTemplateShouldThrowExceptionWhenNoTemplatesFound () {
        ScheduleResponse scheduleResponse = mockScheduleResponse(Collections.emptyList(), Collections.emptyList());

        assertThatThrownBy(() -> scheduleResolver.resolve(scheduleResponse, MONDAY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void buildFromOverrideShouldUnavailable () {
        ScheduleTemplateDTO scheduleTemplateDTO = mockScheduleTemplateDTO(
                "MONDAY", "09:00", "17:00", "12:00", "13:00", "30");
        ScheduleOverrideDTO scheduleOverrideDTO = mockScheduleOverrideDTO(
                MONDAY.toString(), "UNAVAILABLE", null, null, null
        );
        ScheduleResponse scheduleResponse = mockScheduleResponse(List.of(scheduleTemplateDTO), List.of(scheduleOverrideDTO));

        ResolvedSchedule schedule = scheduleResolver.resolve(scheduleResponse, MONDAY);

        assertThat(schedule.unavailable()).isTrue();
    }

    @Test
    void buildFromOverrideShouldSetOverrideIfOverrideIsCustomHoursValue () {
        ScheduleTemplateDTO scheduleTemplateDTO = mockScheduleTemplateDTO(
                "MONDAY", "09:00", "17:00", "12:00", "13:00", "30");
        ScheduleOverrideDTO scheduleOverrideDTO = mockScheduleOverrideDTO(
                MONDAY.toString(), "CUSTOM_HOURS", "14:00", "16:00", "30"
        );
        ScheduleResponse scheduleResponse = mockScheduleResponse(List.of(scheduleTemplateDTO), List.of(scheduleOverrideDTO));

        ResolvedSchedule schedule = scheduleResolver.resolve(scheduleResponse, MONDAY);

        assertThat(schedule.unavailable()).isFalse();
        assertThat(schedule.startTime().toString()).isEqualTo("14:00");
        assertThat(schedule.endTime().toString()).isEqualTo("16:00");
    }

    private ScheduleResponse mockScheduleResponse(
            List<ScheduleTemplateDTO> scheduleTemplateDTOS,
            List<ScheduleOverrideDTO> scheduleOverrideDTOS
    ) {
        ScheduleResponse response = mock(ScheduleResponse.class);
        when(response.listOfScheduleTemplateResponse()).thenReturn(scheduleTemplateDTOS);
        when(response.listOfScheduleOverrideResponse()).thenReturn(scheduleOverrideDTOS);
        return response;
    }

    private ScheduleTemplateDTO mockScheduleTemplateDTO(
            String day, String startTime, String endTime, String breakStartTime, String breakEndTime, String duration
    ) {
        ScheduleTemplateDTO dto = mock(ScheduleTemplateDTO.class);
        when(dto.dayOfTheWeek()).thenReturn(day);
        when(dto.startTime()).thenReturn(startTime);
        when(dto.endTime()).thenReturn(endTime);
        when(dto.breakStartTime()).thenReturn(breakStartTime);
        when(dto.breakEndTime()).thenReturn(breakEndTime);
        when(dto.slotDurationOfMinutes()).thenReturn(duration);
        return dto;
    }

    private ScheduleOverrideDTO mockScheduleOverrideDTO(
            String date, String overrideType, String startTime, String endTime, String duration
    ) {
        ScheduleOverrideDTO dto = mock(ScheduleOverrideDTO.class);
        when(dto.date()).thenReturn(date);
        when(dto.startTime()).thenReturn(startTime);
        when(dto.endTime()).thenReturn(endTime);
        when(dto.overrideType()).thenReturn(overrideType);
        when(dto.slotDurationOfMinutes()).thenReturn(duration);
        return dto;
    }
}