package org.com.meetingservice.mapper;

import lombok.experimental.UtilityClass;
import org.com.meetingservice.dto.MeetingResponse;
import org.com.meetingservice.model.Meeting;

@UtilityClass
public class MeetingMapper {

    public static MeetingResponse toResponseDTO(Meeting meeting) {
        return new MeetingResponse(
                meeting.getId(),
                meeting.getDoctorId().toString(),
                meeting.getPatientId().toString(),
                meeting.getMeetingDateTime().toString(),
                meeting.getDurationOfMinutes().toString(),
                meeting.getStatus().toString(),
                meeting.getNotes()
        );
    }
}