package org.com.meetingservice.service;

import org.com.meetingservice.dto.AvailableSlotResponse;
import org.com.meetingservice.service.filter.BookedSlotStrategy;
import org.com.meetingservice.service.filter.SlotFilterContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class BookedSlotStrategyTest {

    private BookedSlotStrategy strategy;

    private static final LocalDate DATE = LocalDate.of(2025, 7, 7);

    @BeforeEach
    void setUp() {
        strategy = new BookedSlotStrategy();
    }

    @Test
    void filterShouldRemoveBookedSlots() {
        List<AvailableSlotResponse> availableSlotResponses = List.of(
                slot("09:00", "09:30"),
                slot("09:30", "10:00"),
                slot("10:00", "10:30")
        );

        List<LocalDateTime> booked = List.of(
                LocalDateTime.of(2025, 7, 7, 9, 30)
        );

        List<AvailableSlotResponse> responses = strategy.filter(availableSlotResponses, new SlotFilterContext(DATE, booked));

        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses).noneMatch(s -> s.startTime().equals("09:30"));
    }

    @Test
    void filterShouldReturnAllSlotsWhenNoneAreBooked() {
        List<AvailableSlotResponse> availableSlotResponses = List.of(
                slot("09:00", "09:30"),
                slot("09:30", "10:00")
        );

        List<AvailableSlotResponse> responses = strategy.filter(availableSlotResponses, new SlotFilterContext(DATE, List.of()));

        assertThat(responses.size()).isEqualTo(2);
    }

    @Test
    void filterShouldReturnEmptyListWhenAllSlotsAreBooked() {
        List<AvailableSlotResponse> availableSlotResponses = List.of(
                slot("09:00", "09:30"),
                slot("09:30", "10:00")
        );

        List<LocalDateTime> booked = List.of(
                LocalDateTime.of(2025, 7, 7, 9, 0),
                LocalDateTime.of(2025, 7, 7, 9, 30)
        );

        List<AvailableSlotResponse> responses = strategy.filter(availableSlotResponses, new SlotFilterContext(DATE, booked));

        assertThat(responses).isEmpty();
    }

    @Test
    void filterShouldHandelEmptySlotList() {
        List<LocalDateTime> booked = List.of(
                LocalDateTime.of(2025, 7, 7, 9, 0)
                );

        List<AvailableSlotResponse> slotResponses = strategy.filter(List.of(), new SlotFilterContext(DATE, booked));

        assertThat(slotResponses).isEmpty();
    }

    private AvailableSlotResponse slot(
            String startTime,
            String endTime
    ) {
        return new AvailableSlotResponse(DATE.toString(), startTime, endTime, "30");
    }
}