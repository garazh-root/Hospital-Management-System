package org.com.meetingservice.service;

import org.com.meetingservice.dto.AvailableSlotResponse;
import org.com.meetingservice.dto.ResolvedSchedule;
import org.com.meetingservice.dto.ScheduleResponse;
import org.com.meetingservice.service.factory.SlotFactory;
import org.com.meetingservice.service.filter.BookedSlotStrategy;
import org.com.meetingservice.service.filter.SlotFilterContext;
import org.com.meetingservice.service.filter.SlotStrategy;
import org.com.meetingservice.service.resolver.ScheduleResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SlotGeneratorServiceTest {

    @Mock
    private BookedSlotStrategy bookedSlotStrategyA;

    @Mock
    private BookedSlotStrategy bookedSlotStrategyB;

    @Mock
    private ScheduleResolver scheduleResolver;

    private SlotStrategy slotStrategy;

    private SlotGeneratorService slotGeneratorService;

    @Mock
    private SlotFactory slotFactory;

    private static final LocalDate DATE = LocalDate.of(2025, 5, 1);

    @Test
    void generateSlotsShouldReturnEmptyListWhenDoctorIsUnavailable() {
        slotGeneratorService = new SlotGeneratorService(slotFactory, scheduleResolver, Collections.emptyList());
        ScheduleResponse scheduleResponse = mock(ScheduleResponse.class);

        when(scheduleResolver.resolve(scheduleResponse, DATE)).thenReturn(ResolvedSchedule.notAvailable());

        List<AvailableSlotResponse> responses = slotGeneratorService.generateAvailableSlots(scheduleResponse, DATE, Collections.emptyList());

        assertThat(responses).isEmpty();
    }

    @Test
    void generateSlotsShouldReturnAdjustedSlotsWhenAllFilterApplied() {
        slotGeneratorService = new SlotGeneratorService(slotFactory, scheduleResolver, List.of(bookedSlotStrategyA, bookedSlotStrategyB));
        ScheduleResponse scheduleResponse = mock(ScheduleResponse.class);

        ResolvedSchedule resolvedSchedule = ResolvedSchedule.fromOverride(
                LocalTime.of(9, 0), LocalTime.of(10, 0), 30
        );

        List<AvailableSlotResponse> rawSlots = List.of(
                slot("09:00"), slot("09:30")
        );
        List<AvailableSlotResponse> afterFilterA = List.of(slot("09:00"), slot("09:30"));
        List<AvailableSlotResponse> afterFilterB = List.of(slot("09:00"));

        when(scheduleResolver.resolve(scheduleResponse, DATE)).thenReturn(resolvedSchedule);
        when(slotFactory.generateSlots(DATE, resolvedSchedule)).thenReturn(rawSlots);
        when(bookedSlotStrategyA.filter(eq(rawSlots), any(SlotFilterContext.class))).thenReturn(afterFilterA);
        when(bookedSlotStrategyB.filter(eq(afterFilterA), any(SlotFilterContext.class))).thenReturn(afterFilterB);

        List<AvailableSlotResponse> result = slotGeneratorService.generateAvailableSlots(scheduleResponse, DATE, Collections.emptyList());

        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void generateSlotsShouldPassBookedMeetingsInFilterContext() {
        slotGeneratorService = new SlotGeneratorService(slotFactory, scheduleResolver, List.of(bookedSlotStrategyA));
        ScheduleResponse scheduleResponse = mock(ScheduleResponse.class);

        ResolvedSchedule resolvedSchedule = ResolvedSchedule.fromOverride(
                LocalTime.of(9, 0), LocalTime.of(10, 0), 30
        );

        List<LocalDateTime> bookedMeetings = List.of(LocalDateTime.of(2025, 7, 7, 9, 0));

        when(scheduleResolver.resolve(scheduleResponse, DATE)).thenReturn(resolvedSchedule);
        when(slotFactory.generateSlots(any(), any())).thenReturn(List.of());
        when(bookedSlotStrategyA.filter(any(), any())).thenReturn(List.of());

        List<AvailableSlotResponse> result = slotGeneratorService.generateAvailableSlots(scheduleResponse, DATE, bookedMeetings);

        verify(bookedSlotStrategyA).filter(any(), argThat(ctx ->
                ctx.date().equals(DATE) && ctx.bookedMeetings().equals(bookedMeetings)));
    }

    @Test
    void generateSlotsShouldWorkWithNoFilters() {
        slotGeneratorService = new SlotGeneratorService(slotFactory, scheduleResolver, Collections.emptyList());
        ScheduleResponse scheduleResponse = mock(ScheduleResponse.class);

        ResolvedSchedule resolvedSchedule = ResolvedSchedule.fromOverride(
                LocalTime.of(9, 0), LocalTime.of(10, 0), 30
        );

        List<AvailableSlotResponse> rawSlots = List.of(slot("09:00"), slot("09:30"));

        when(scheduleResolver.resolve(scheduleResponse, DATE)).thenReturn(resolvedSchedule);
        when(slotFactory.generateSlots(any(), any())).thenReturn(rawSlots);

        List<AvailableSlotResponse> result = slotGeneratorService.generateAvailableSlots(scheduleResponse, DATE, Collections.emptyList());

        assertThat(result.size()).isEqualTo(2);
    }

    private AvailableSlotResponse slot(String startTime) {
        return new AvailableSlotResponse(DATE.toString(), startTime, "00:00", "30");
    }
}