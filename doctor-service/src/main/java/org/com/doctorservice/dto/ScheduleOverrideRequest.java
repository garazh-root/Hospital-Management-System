package org.com.doctorservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleOverrideRequest {
    @NotNull(message = "Date required")
    private LocalDate date;

    @NotBlank(message = "Override type is required (EMERGENCY, CUSTOM_HOURS, UNAVAILABLE)")
    private String overrideType;

    @NotNull(message = "Start time required")
    private LocalTime startTime;

    @NotNull(message = "End time required")
    private LocalTime endTime;

    @NotNull(message = "Slot duration required")
    @Max(value = 30, message = "Slot duration must be not more than 30 minutes")
    private Integer slotDurationOfMinutes;

    private String reason;
}