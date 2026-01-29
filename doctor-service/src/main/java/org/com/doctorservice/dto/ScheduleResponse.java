package org.com.doctorservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleResponse {

    private List<ScheduleTemplateResponse> listOfScheduleTemplateResponse;
    private List<ScheduleOverrideResponse> listOfScheduleOverrideResponse;
}