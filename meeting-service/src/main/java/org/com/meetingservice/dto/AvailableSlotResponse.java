package org.com.meetingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AvailableSlotResponse {
    private String date;
    private String startTime;
    private String endTime;
    private String duration;
}