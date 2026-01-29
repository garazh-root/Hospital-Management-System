package org.com.doctorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleResponse {

    private List<ScheduleTemplateResponse> listOfScheduleTemplateResponse;
    private List<ScheduleOverrideResponse> listOfScheduleOverrideResponse;
}