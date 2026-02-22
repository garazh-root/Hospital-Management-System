package org.com.meetingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingResponse {

    private String id;
    private String doctorId;
    private String patientId;
    private String meetingDateTime;
    private String durationOfMinutes;
    private String meetingStatus;
    private String notes;
}