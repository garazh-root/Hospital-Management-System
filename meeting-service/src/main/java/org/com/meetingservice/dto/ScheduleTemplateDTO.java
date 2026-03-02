package org.com.meetingservice.dto;

public record ScheduleTemplateDTO(
        String id,
        String doctorId,
        String dayOfTheWeek,
        String startTime,
        String endTime,
        String breakStartTime,
        String breakEndTime,
        String slotDurationOfMinutes,
        String effectiveFrom,
        String effectiveTo) {
}