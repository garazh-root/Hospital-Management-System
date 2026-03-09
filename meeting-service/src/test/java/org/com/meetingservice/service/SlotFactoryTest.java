package org.com.meetingservice.service;

import org.com.meetingservice.dto.AvailableSlotResponse;
import org.com.meetingservice.dto.ResolvedSchedule;
import org.com.meetingservice.service.factory.SlotFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SlotFactoryTest {

    private SlotFactory slotFactory;

    private final LocalDate DATE = LocalDate.of(2026, 6, 1);

    @BeforeEach
    void setUp() {
        slotFactory = new SlotFactory();
    }

    @Test
    void generateSlotsShouldCorrectlyReturnSlotsBasedOnResolvedSchedule() {
        ResolvedSchedule resolvedSchedule = ResolvedSchedule.fromOverride(
                LocalTime.of(9, 0), LocalTime.of(11, 0), 30
        );

        List<AvailableSlotResponse> slots = slotFactory.generateSlots(DATE, resolvedSchedule);

        assertThat(slots).hasSize(4);
    }

    @Test
    void generateSlotsShouldCorrectlySetStartAndEndTimeInReturningSlots() {
        ResolvedSchedule resolvedSchedule = ResolvedSchedule.fromOverride(
                LocalTime.of(9, 0), LocalTime.of(10, 0), 30
        );

        List<AvailableSlotResponse> slots = slotFactory.generateSlots(DATE, resolvedSchedule);

        assertThat(slots.get(0).startTime().equals("09:00"));
        assertThat(slots.get(0).endTime().equals("09:30"));
        assertThat(slots.get(1).startTime().equals("09:30"));
        assertThat(slots.get(1).endTime().equals("10:00"));
    }

    @Test
    void generateSlotsShouldSkipSlotsThatAreOverlappingWithBreakTime() {
        ResolvedSchedule resolvedSchedule = ResolvedSchedule.fromTemplate(
                LocalTime.of(9, 0), LocalTime.of(13, 0),
                LocalTime.of(11, 0), LocalTime.of(12, 0),
                30
        );

        List<AvailableSlotResponse> slots = slotFactory.generateSlots(DATE, resolvedSchedule);

        List<String> timeSlots = slots.stream().map(AvailableSlotResponse::startTime).toList();

        assertThat(timeSlots).containsExactly("09:00", "09:30", "10:00", "10:30", "12:00", "12:30");
        assertThat(timeSlots).doesNotContain("11:00", "11:30");
    }

    @Test
    void generateSlotsShouldNotContainPartSlots() {
        ResolvedSchedule resolvedSchedule = ResolvedSchedule.fromTemplate(
                LocalTime.of(9, 0), LocalTime.of(13, 0),
                LocalTime.of(11, 0), LocalTime.of(12, 0),
                30
        );

        List<AvailableSlotResponse> slots = slotFactory.generateSlots(DATE, resolvedSchedule);

        List<String> timeSlots = slots.stream().map(AvailableSlotResponse::startTime).toList();

        assertThat(timeSlots).doesNotContain("10:45");
        assertThat(timeSlots).contains("12:00");
    }

    @Test
    void generateSlotsShouldReturnEmptyIfSlotEndTimeIsLessThanDuration() {
        ResolvedSchedule resolvedSchedule = ResolvedSchedule.fromOverride(
                LocalTime.of(9, 0), LocalTime.of(9, 20), 30
        );

        List<AvailableSlotResponse> slots = slotFactory.generateSlots(DATE, resolvedSchedule);

        assertThat(slots).isEmpty();
    }

    @Test
    void generateSLotsShouldReturnSlotsWithCertainDateInAllSlots() {
        ResolvedSchedule resolvedSchedule = ResolvedSchedule.fromOverride(
                LocalTime.of(9, 0), LocalTime.of(9, 20), 30
        );

        List<AvailableSlotResponse> slots = slotFactory.generateSlots(DATE, resolvedSchedule);

        assertThat(slots).allMatch(slot -> LocalDate.parse(slot.date()).equals(DATE));
    }
}