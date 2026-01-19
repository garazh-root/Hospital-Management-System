package org.com.meetingservice.mapper;

import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.dto.DoctorResponseDTO;
import org.com.meetingservice.dto.MeetingResponse;
import org.com.meetingservice.dto.PatientResponseDTO;
import org.com.meetingservice.dto.ScheduleResponseDTO;
import org.com.meetingservice.model.Meeting;
import org.com.meetingservice.requests.BookingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class MeetingMapperTest {

    private BookingRequest bookingRequest;
    private DoctorResponseDTO doctorResponseDTO;
    private PatientResponseDTO patientResponseDTO;
    private Meeting meeting;

    @BeforeEach
    void setup() {

        String meetingId = "fd7ffdc471ff7dff5fcb1faf";
        String doctorId = UUID.randomUUID().toString();
        String patientId = UUID.randomUUID().toString();

        LocalDate mondayMeetingDate = LocalDate.of(2026, 2, 1);

        Instant meetingStartTime = mondayMeetingDate
                .atTime(10, 0)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        Instant meetingEndTime = mondayMeetingDate
                .atTime(11, 0)
                .atZone(ZoneId.systemDefault())
                .toInstant();

        Instant meetingDate = mondayMeetingDate
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant();

        this.bookingRequest = BookingRequest.builder()
                .doctorId(doctorId)
                .patientId(patientId)
                .meetingDate(meetingDate)
                .meetingStartTime(meetingStartTime)
                .meetingEndTime(meetingEndTime)
                .notes("Test")
                .build();

        ScheduleResponseDTO firstDoctorSchedule = ScheduleResponseDTO.builder()
                .scheduleStartTime("09:00:00")
                .scheduleEndTime("17:00:00")
                .scheduleDate("2026-02-01")
                .breakStartTime("12:00:00")
                .breakEndTime("13:00:00")
                .dayOfWeek("MONDAY")
                .isDayOff("false")
                .build();

        ScheduleResponseDTO secondDoctorSchedule = ScheduleResponseDTO.builder()
                .scheduleStartTime("10:00:00")
                .scheduleEndTime("16:00:00")
                .scheduleDate("2026-02-03")
                .breakStartTime("12:00:00")
                .breakEndTime("13:00:00")
                .dayOfWeek("WEDNESDAY")
                .isDayOff("false")
                .build();

        this.doctorResponseDTO = DoctorResponseDTO.builder()
                .id(doctorId)
                .firstName("Steven")
                .lastName("Rondon")
                .gender("MALE")
                .email("stevethedoc@test.com")
                .phoneNumber("+1270339512")
                .specialization("Surgeon")
                .rating("0.0")
                .doctorStatus("ACTIVE")
                .schedulesList(List.of(firstDoctorSchedule, secondDoctorSchedule))
                .build();

        this.patientResponseDTO = PatientResponseDTO.builder()
                .id(patientId)
                .firstName("Abella")
                .lastName("Cruz")
                .gender("FEMALE")
                .weight("65.3")
                .height("171.2")
                .email("mechanicbella@test.com")
                .phoneNumber("+1230392345")
                .dateOfBirth("2003-11-29")
                .address("Alexey 23 apt.5")
                .status("ACTIVE")
                .build();

        this.meeting = Meeting.builder()
                .id(meetingId)
                .doctorId(UUID.fromString(doctorResponseDTO.getId()))
                .patientId(UUID.fromString(patientResponseDTO.getId()))
                .doctorFirstName(doctorResponseDTO.getFirstName())
                .doctorLastName(doctorResponseDTO.getLastName())
                .doctorSpecialization(doctorResponseDTO.getSpecialization())
                .patientFirstName(patientResponseDTO.getFirstName())
                .patientLastName(patientResponseDTO.getLastName())
                .date(bookingRequest.getMeetingDate())
                .startTime(bookingRequest.getMeetingStartTime())
                .endTime(bookingRequest.getMeetingEndTime())
                .status(MeetingStatus.CONFIRMED)
                .notes("Test")
                .build();
    }

    @Test
    void toEntityShouldSuccessfullyMapToModel() {
        Meeting meeting = MeetingMapper.toEntity(bookingRequest, doctorResponseDTO,  patientResponseDTO);

        assertEquals(meeting.getDoctorId().toString(), doctorResponseDTO.getId());
        assertEquals(meeting.getPatientId().toString(), patientResponseDTO.getId());
        assertEquals(meeting.getDoctorFirstName(), doctorResponseDTO.getFirstName());
        assertEquals(meeting.getDoctorLastName(), doctorResponseDTO.getLastName());
        assertEquals(meeting.getDoctorSpecialization(), doctorResponseDTO.getSpecialization());
        assertEquals(meeting.getPatientFirstName(), patientResponseDTO.getFirstName());
        assertEquals(meeting.getPatientLastName(), patientResponseDTO.getLastName());
        assertEquals(meeting.getDate(), bookingRequest.getMeetingDate());
        assertEquals(meeting.getStartTime(), bookingRequest.getMeetingStartTime());
        assertEquals(meeting.getEndTime(), bookingRequest.getMeetingEndTime());
        assertEquals(meeting.getNotes(), bookingRequest.getNotes());
    }

    @Test
    void toResponseShouldSuccessfullyMapToResponseDTO() {
        MeetingResponse meetingResponse = MeetingMapper.toResponse(meeting);

        assertEquals(meetingResponse.getMeetingDate(), bookingRequest.getMeetingDate().toString());
        assertEquals(meetingResponse.getStartTime(), bookingRequest.getMeetingStartTime().toString());
        assertEquals(meetingResponse.getEndTime(), bookingRequest.getMeetingEndTime().toString());
        assertEquals(meetingResponse.getNotes(), bookingRequest.getNotes());
    }
}