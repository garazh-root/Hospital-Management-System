package org.com.meetingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleTemplateDTO {
    private String id;
    private String doctorId;
    private String dayOfTheWeek;
    private String startTime;
    private String endTime;
    private String breakStartTime;
    private String breakEndTime;
    private String slotDurationOfMinutes;
    private String effectiveFrom;
    private String effectiveTo;
}