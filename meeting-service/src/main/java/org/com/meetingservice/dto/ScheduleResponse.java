package org.com.meetingservice.dto;

import java.util.List;

public record ScheduleResponse(
    List<ScheduleTemplateDTO> listOfScheduleTemplateResponse,
    List<ScheduleOverrideDTO> listOfScheduleOverrideResponse) {
}