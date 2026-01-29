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
public class ScheduleTemplateRequest {
    @NotBlank(message = "Day of week required")
    private String dayOfTheWeek;

    @NotNull(message = "Start time required")
    private LocalTime startTime;

    @NotNull(message = "End time required")
    private LocalTime endTime;

    @NotNull(message = "Break start time required")
    private LocalTime breakStartTime;

    @NotNull(message = "Break end time required")
    private LocalTime breakEndTime;

    @NotNull(message = "Slot duration of minutes required")
    @Max(value = 30, message = "Duration must not be more than 30 minutes")
    private Integer slotDuration;

    @NotNull(message = "Effective date from required")
    private LocalDate effectiveFrom;

    @NotNull(message = "Effective date to required")
    private LocalDate effectiveTo;
}