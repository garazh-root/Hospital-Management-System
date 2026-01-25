package org.com.meetingservice.validation;

import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.dto.DoctorResponseDTO;
import org.com.meetingservice.dto.ScheduleResponseDTO;
import org.com.meetingservice.exception.*;
import org.com.meetingservice.model.Meeting;
import org.com.meetingservice.repository.MeetingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorValidationTest {

    @InjectMocks
    private DoctorValidation doctorValidation;

    @Mock
    private MeetingRepository meetingRepository;

    private DoctorResponseDTO doctor;
    private String doctorId;
    private String meetingId;
    private Instant start;
    private Instant end;
    private ScheduleResponseDTO schedule;

    @BeforeEach
    void setUp() {

        doctorId = UUID.randomUUID().toString();
        meetingId = "69767527b7b3e35db2e220c6";

        schedule = ScheduleResponseDTO.builder()
                .scheduleStartTime("09:00:00")
                .scheduleEndTime("17:00:00")
                .scheduleDate("")
                .breakStartTime("12:00:00")
                .breakEndTime("13:00:00")
                .dayOfWeek("WEDNESDAY")
                .isDayOff("false")
                .build();

        doctor = DoctorResponseDTO.builder()
                .id(doctorId)
                .firstName("Steven")
                .lastName("Rondon")
                .gender("MALE")
                .email("stevethedoc@test.com")
                .phoneNumber("+1270339512")
                .specialization("Surgeon")
                .rating("0.0")
                .doctorStatus("ACTIVE")
                .schedulesList(List.of(schedule))
                .build();

        start = Instant.parse("2024-03-20T14:00:00Z");
        end = Instant.parse("2024-03-20T15:00:00Z");
    }

    @Test
    void checkDoctorStatusShouldNotThrowExceptionWhenDoctorIsActive() {
        assertDoesNotThrow(() -> doctorValidation.checkDoctorStatus(doctor));
    }

    @Test
    void checkDoctorStatusShouldThrowExceptionWhenDoctorIsNotActive() {
        doctor.setDoctorStatus("INACTIVE");

        assertThrows(InvalidStatusException.class, () -> doctorValidation.checkDoctorStatus(doctor));
    }

    @Test
    void checkDoctorStatusShouldThrowExceptionWhenStatusIsNull() {
        assertThrows(InvalidStatusException.class, () -> doctorValidation.checkDoctorStatus(null));
    }

    @Test
    void checkDoctorScheduleShouldThrowExceptionWhenScheduleIsNull() {
        doctor.setSchedulesList(null);

        assertThrows(ScheduleNotAvailableException.class, () -> doctorValidation.checkDoctorSchedule(
                doctor, start, end));
    }

    @Test
    void checkDoctorScheduleShouldThrowExceptionWhenScheduleIsEmpty() {
        doctor.setSchedulesList(Collections.emptyList());

        assertThrows(ScheduleNotAvailableException.class, () -> doctorValidation.checkDoctorSchedule(
                doctor, start, end));
    }

    @Test
    void checkDoctorScheduleShouldThrowExceptionWhenIsNoMatchWithDayOfTheWeek() {
        Instant thursdayStart = Instant.parse("2024-03-21T10:00:00Z");
        Instant thursdayEnd = Instant.parse("2024-03-21T11:00:00Z");

        Assertions.assertThrows(ScheduleNotMatchingException.class, () -> doctorValidation.checkDoctorSchedule(
                doctor, thursdayStart, thursdayEnd));
    }

    @Test
    void checkDoctorScheduleShouldThrowExceptionWhenInScheduleIsDayOff() {
        schedule.setIsDayOff("true");

        Assertions.assertThrows(ScheduleNotAvailableException.class, () -> doctorValidation.checkDoctorSchedule(
                doctor, start, end
        ));
    }

    @Test
    void checkDoctorScheduleShouldThrowExceptionWhenStartTimeIsBeforeScheduleStartTime() {
        Instant earlyStart = Instant.parse("2024-03-20T08:00:00Z");
        Instant earlyEnd = Instant.parse("2024-03-20T09:00:00Z");

        Assertions.assertThrows(ScheduleNotAvailableException.class, () -> doctorValidation.checkDoctorSchedule(
                doctor, earlyStart, earlyEnd
        ));
    }

    @Test
    void checkDoctorScheduleShouldThrowExceptionWhenEndTimeIsAfterScheduleStartTime() {
        Instant lateStart = Instant.parse("2024-03-20T18:00:00Z");
        Instant lateEnd = Instant.parse("2024-03-20T19:00:00Z");

        Assertions.assertThrows(ScheduleNotAvailableException.class, () -> doctorValidation.checkDoctorSchedule(
                doctor, lateStart, lateEnd
        ));
    }

    @Test
    void checkDoctorScheduleShouldThrowExceptionWhenDoctorBreakTimeOverlap() {
        Instant start = Instant.parse("2024-03-20T12:30:00Z");
        Instant end = Instant.parse("2024-03-20T13:30:00Z");

        Assertions.assertThrows(InvalidMeetingException.class, () -> doctorValidation.checkDoctorSchedule(
                doctor, start, end
        ));
    }

    @Test
    void checkDoctorScheduleShouldMatchSpecificDate() {
        schedule.setScheduleDate("2024-03-20");

        assertDoesNotThrow(() -> doctorValidation.checkDoctorSchedule(doctor, start, end));
    }

    @Test
    void checkDoctorAvailabilityShouldNotThrowExceptionWhenIsNoConflicts() {
        when(meetingRepository.findByDoctorIdAndStatusAndStartTimeBetween(
                any(UUID.class), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class)
        )).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> doctorValidation.checkDoctorAvailability(doctorId, start, end));

        verify(meetingRepository, times(1)).findByDoctorIdAndStatusAndStartTimeBetween(
                any(UUID.class), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class)
        );

    }

    @Test
    void checkDoctorAvailabilityShouldThrowExceptionWhenConflictOccur () {
        Meeting meeting = Meeting.builder()
                .id(meetingId)
                .doctorId(UUID.fromString(doctorId))
                .startTime(Instant.parse("2024-03-20T14:30:00Z"))
                .endTime(Instant.parse("2024-03-20T15:30:00Z"))
                .status(MeetingStatus.CONFIRMED)
                .build();

        when(meetingRepository.findByDoctorIdAndStatusAndStartTimeBetween(
                any(UUID.class), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class)
        )).thenReturn(List.of(meeting));

        Assertions.assertThrows(MeetingConflictException.class, () -> doctorValidation.checkDoctorAvailability(
                doctorId, start, end
        ));

        verify(meetingRepository, times(1)).findByDoctorIdAndStatusAndStartTimeBetween(
                any(UUID.class), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class)
        );
    }

    @Test
    void checkDoctorAvailabilityShouldNotThrowExceptionWhenNoConflicts() {
        Meeting meeting = Meeting.builder()
                .id(meetingId)
                .doctorId(UUID.fromString(doctorId))
                .startTime(Instant.parse("2024-03-20T10:30:00Z"))
                .endTime(Instant.parse("2024-03-20T11:30:00Z"))
                .status(MeetingStatus.CONFIRMED)
                .build();

        when(meetingRepository.findByDoctorIdAndStatusAndStartTimeBetween(
                any(UUID.class), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class)
        )).thenReturn(List.of(meeting));

        assertDoesNotThrow(() -> doctorValidation.checkDoctorAvailability(
                doctorId, start, end
        ));

        verify(meetingRepository, times(1)).findByDoctorIdAndStatusAndStartTimeBetween(
                any(UUID.class), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class)
        );
    }

}