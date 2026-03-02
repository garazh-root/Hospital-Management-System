package org.com.meetingservice.requests;

import java.time.LocalDateTime;
import java.util.UUID;

public record MeetingRequest(
        UUID doctorId,
        UUID patientId,
        LocalDateTime meetingDateTime,
        Integer duration,
        String reason) {

    public MeetingRequest{
        if(doctorId == null || patientId == null) {
            throw new NullPointerException("doctorId and patientId cannot be null");
        }else if(meetingDateTime == null || duration == null) {
            throw new NullPointerException("meetingDateTime and duration cannot be null");
        }
    }
}