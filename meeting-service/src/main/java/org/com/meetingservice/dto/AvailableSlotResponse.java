package org.com.meetingservice.dto;

public record AvailableSlotResponse (
     String date,
     String startTime,
     String endTime,
     String duration) {
}