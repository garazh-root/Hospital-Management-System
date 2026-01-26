package org.com.meetingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.meetingservice.info.DoctorInfo;
import org.com.meetingservice.info.PatientInfo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingResponse {
    private String id;

    private String meetingDate;
    private String startTime;
    private String endTime;
    private String meetingStatus;
    private String notes;

    private DoctorInfo doctor;
    private PatientInfo patient;

    private String createdAt;
    private String updatedAt;
}