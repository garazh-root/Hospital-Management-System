package org.com.meetingservice.dto;

import lombok.AllArgsConstructor;import lombok.Builder;import lombok.Data;import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleOverrideDTO {
    private String id;
    private String doctorId;
    private String date;
    private String overrideType;
    private String startTime;
    private String endTime;
    private String slotDurationOfMinutes;
    private String reason;
}