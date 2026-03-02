package org.com.meetingservice.dto;

public record ScheduleOverrideDTO (
     String id,
     String doctorId,
     String date,
     String overrideType,
     String startTime,
     String endTime,
     String slotDurationOfMinutes,
     String reason)  {
}