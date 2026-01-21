package org.com.meetingservice.validation;

import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.dto.PatientResponseDTO;
import org.com.meetingservice.exception.InvalidStatusException;
import org.com.meetingservice.exception.MeetingConflictException;
import org.com.meetingservice.model.Meeting;
import org.com.meetingservice.repository.MeetingRepository;
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
public class PatientValidationTest {

    @InjectMocks
    private PatientValidation patientValidation;

    @Mock
    private MeetingRepository meetingRepository;

    private PatientResponseDTO patientResponse;
    private String patientId;
    private Instant start;
    private Instant end;

    @BeforeEach
    void setUp() {
        this.patientId = UUID.randomUUID().toString();

        this.patientResponse = PatientResponseDTO.builder()
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

        start = Instant.parse("2024-03-20T10:00:00Z");
        end = Instant.parse("2024-03-20T11:00:00Z");
    }

    @Test
    void checkPatientStatusShouldThrowInvalidStatusExceptionWhenStatusInNull() {

        assertThrows(InvalidStatusException.class, () ->
                patientValidation.checkPatientStatus(null));
    }


    @Test
    void checkPatientStatusShouldThrowExceptionWhenStatusIsNotActive() {
        patientResponse.setStatus("INACTIVE");

        assertThrows(InvalidStatusException.class, () ->
                patientValidation.checkPatientStatus(patientResponse));
    }

    @Test
    void checkPatientStatusShouldNotThrowExceptionWhenStatusIsActive() {

        assertDoesNotThrow(() -> patientValidation.checkPatientStatus(patientResponse));
    }

    @Test
    void checkPatientAvailabilityShouldNotThrowExceptionWhenNoConflictsOccur() {
        when(meetingRepository.findByPatientIdAndStatusAndStartTimeBetween(
                any(UUID.class), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class))).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> patientValidation.checkPatientAvailability(
                patientResponse.getId(), start, end
        ));

        verify(meetingRepository).findByPatientIdAndStatusAndStartTimeBetween(
                eq(UUID.fromString(patientId)), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class)
        );
    }

    @Test
    void checkPatientAvailabilityShouldNotThrowExceptionWhenMeetingExistsButNotOverlap () {
        Meeting noConflictMeeting = createMeeting(
                Instant.parse("2024-03-20T08:00:00Z"),
                Instant.parse("2024-03-20T09:00:00Z"));

        when(meetingRepository.findByPatientIdAndStatusAndStartTimeBetween(
                any(UUID.class), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class))).thenReturn(
                        List.of(noConflictMeeting));

        assertDoesNotThrow(() -> patientValidation.checkPatientAvailability(
                patientResponse.getId(), start, end
        ));
    }

    @Test
    void checkPatientAvailabilityShouldThrowExceptionWhenExactOverlap () {
        Meeting conflictMeeting = createMeeting(start, end);

        when(meetingRepository.findByPatientIdAndStatusAndStartTimeBetween(
                any(UUID.class), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class))).thenReturn(List.of(conflictMeeting));

        assertThrows(MeetingConflictException.class, () ->
                patientValidation.checkPatientAvailability(patientResponse.getId(), start, end));
    }

    @Test
    void checkPatientAvailabilityShouldThrowExceptionWhenOverlapAtStartOccurs () {
        Meeting conflictMeeting = createMeeting(
                Instant.parse("2024-03-20T09:30:00Z"),
                Instant.parse("2024-03-20T10:30:00Z"));

        when(meetingRepository.findByPatientIdAndStatusAndStartTimeBetween(
                any(UUID.class), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class))).thenReturn(List.of(conflictMeeting));

        assertThrows(MeetingConflictException.class, () ->
                patientValidation.checkPatientAvailability(patientResponse.getId(), start, end));
    }

    @Test
    void checkPatientAvailabilityShouldThrowExceptionWhenOverlapAtEndOccurs () {
        Meeting conflictMeeting = createMeeting(
                Instant.parse("2024-03-20T10:30:00Z"),
                Instant.parse("2024-03-20T11:30:00Z"));

        when(meetingRepository.findByPatientIdAndStatusAndStartTimeBetween(
                any(UUID.class), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class))).thenReturn(List.of(conflictMeeting));

        assertThrows(MeetingConflictException.class, () ->
                patientValidation.checkPatientAvailability(patientResponse.getId(), start, end));
    }

    @Test
    void checkPatientAvailabilityShouldThrowExceptionWhenMultipleMeetingsOverlapEachOther () {
        Meeting conflictMeeting = createMeeting(
                Instant.parse("2024-03-20T08:00:00Z"),
                Instant.parse("2024-03-20T09:00:00Z"));

        Meeting conflictMeeting1 = createMeeting(
                Instant.parse("2024-03-20T10:30:00Z"),
                Instant.parse("2024-03-20T11:30:00Z"));

        when(meetingRepository.findByPatientIdAndStatusAndStartTimeBetween(
                any(UUID.class), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class))).thenReturn(List.of(conflictMeeting,  conflictMeeting1));

        assertThrows(MeetingConflictException.class, () ->
                patientValidation.checkPatientAvailability(patientResponse.getId(), start, end));
    }

    @Test
    void checkPatientAvailabilityShouldNotThrowExceptionWhenMeetingsOnBoundary () {
        Meeting conflictMeeting = createMeeting(
                Instant.parse("2024-03-20T09:00:00Z"),
                start);

        when(meetingRepository.findByPatientIdAndStatusAndStartTimeBetween(
                any(UUID.class), eq(MeetingStatus.CONFIRMED), any(Instant.class), any(Instant.class)
        )).thenReturn(List.of(conflictMeeting));

        assertDoesNotThrow(() -> patientValidation.checkPatientAvailability(patientResponse.getId(), start, end));
    }

    private Meeting createMeeting(Instant start, Instant end) {
        Meeting meeting = new Meeting();
        meeting.setId("69710d830415567828e96eab");
        meeting.setPatientId(UUID.fromString(patientId));
        meeting.setStartTime(start);
        meeting.setEndTime(end);
        meeting.setStatus(MeetingStatus.CONFIRMED);
        return meeting;

    }
}