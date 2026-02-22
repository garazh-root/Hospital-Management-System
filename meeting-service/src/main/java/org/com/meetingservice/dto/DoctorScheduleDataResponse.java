package org.com.meetingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorScheduleDataResponse {
    private List<ScheduleTemplateDTO>  scheduleTemplates;
    private List<ScheduleTemplateDTO>  scheduleOverrides;
}