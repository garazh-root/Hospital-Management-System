package org.com.meetingservice.validation;

import org.com.meetingservice.dto.DoctorResponseDTO;
import org.com.meetingservice.dto.ScheduleResponseDTO;
import org.com.meetingservice.exception.InvalidStatusException;
import org.com.meetingservice.exception.ScheduleNotAvailableException;
import org.com.meetingservice.exception.ScheduleNotMatchingException;
import org.com.meetingservice.repository.MeetingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
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
    private Instant start;
    private Instant end;
    private ScheduleResponseDTO schedule;

    @BeforeEach
    void setUp() {

        doctorId = UUID.randomUUID().toString();

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

        start = Instant.parse("2024-03-20T10:00:00Z");
        end = Instant.parse("2024-03-20T11:00:00Z");
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
    void checkDoctorStatusShouldThrowExceptionWhenStatusIsNull () {
        assertThrows(InvalidStatusException.class, () -> doctorValidation.checkDoctorStatus(null));
    }

    @Test
    void checkDoctorScheduleShouldThrowExceptionWhenScheduleIsNull() {
        doctor.setSchedulesList(null);

        assertThrows(ScheduleNotAvailableException.class,() -> doctorValidation.checkDoctorSchedule(
                doctor, start, end));
    }

    @Test
    void checkDoctorScheduleShouldThrowExceptionWhenScheduleIsEmpty() {
        doctor.setSchedulesList(Collections.emptyList());

        assertThrows(ScheduleNotAvailableException.class,() -> doctorValidation.checkDoctorSchedule(
                doctor, start, end));
    }

    @Test
    void checkDoctorScheduleShouldThrowExceptionWhenIsNoMatchWithDayOfTheWeek() {
        Instant thursdayStart = Instant.parse("2024-03-21T10:00:00Z");
        Instant thursdayEnd = Instant.parse("2024-03-21T11:00:00Z");

        Assertions.assertThrows(ScheduleNotMatchingException.class, () -> doctorValidation.checkDoctorSchedule(
                doctor, thursdayStart, thursdayEnd));
    }


}