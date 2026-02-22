package org.com.meetingservice.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingRequest {
    private UUID doctorId;
    private UUID patientId;
    private LocalDateTime meetingDateTime;
    private Integer duration;
    private String reason;
}