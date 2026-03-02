package org.com.meetingservice.dto;

public record MeetingResponse (
     String id,
     String doctorId,
     String patientId,
     String meetingDateTime,
     String durationOfMinutes,
     String meetingStatus,
     String notes) {
}