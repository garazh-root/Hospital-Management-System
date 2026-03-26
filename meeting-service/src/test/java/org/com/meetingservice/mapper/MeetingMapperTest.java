package org.com.meetingservice.mapper;

import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.dto.MeetingResponse;
import org.com.meetingservice.model.Meeting;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class MeetingMapperTest {

    @Test
    void toResponseShouldCorrectlyMapMeeting () {
        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        Instant dateTime = Instant.now();

        Meeting meeting = Meeting.builder()
                .id("ddcf0ec6fc0e407ab460bc37")
                .doctorId(doctorId)
                .patientId(patientId)
                .meetingDateTime(dateTime)
                .durationOfMinutes(30)
                .status(MeetingStatus.CONFIRMED)
                .notes("Test")
                .build();

        MeetingResponse meetingResponse = MeetingMapper.toResponseDTO(meeting);

        assertThat(meetingResponse.doctorId()).isEqualTo(doctorId.toString());
        assertThat(meetingResponse.patientId()).isEqualTo(patientId.toString());
        assertThat(meetingResponse.meetingDateTime()).isEqualTo(dateTime.toString());
        assertThat(meetingResponse.durationOfMinutes()).isEqualTo(Integer.valueOf(30).toString());
    }
}