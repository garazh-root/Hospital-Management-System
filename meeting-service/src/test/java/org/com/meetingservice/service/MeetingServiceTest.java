package org.com.meetingservice.service;

import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.client.DoctorClient;
import org.com.meetingservice.client.PatientClient;
import org.com.meetingservice.dto.DoctorResponseDTO;
import org.com.meetingservice.dto.MeetingResponse;
import org.com.meetingservice.dto.PatientResponseDTO;
import org.com.meetingservice.dto.ScheduleResponseDTO;
import org.com.meetingservice.exception.*;
import org.com.meetingservice.model.Meeting;
import org.com.meetingservice.repository.MeetingRepository;
import org.com.meetingservice.requests.BookingRequest;
import org.com.meetingservice.requests.UpdateRequest;
import org.com.meetingservice.validation.DoctorValidation;
import org.com.meetingservice.validation.PatientValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
public class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private DoctorClient doctorClient;

    @Mock
    private PatientClient patientClient;

    @Mock
    private DoctorValidation doctorValidation;

    @Mock
    private PatientValidation patientValidation;

    @InjectMocks
    private MeetingService meetingService;

    private BookingRequest bookingRequest;
    private DoctorResponseDTO doctorResponseDTO;
    private PatientResponseDTO patientResponseDTO;
    private UpdateRequest updateRequest;
    private Meeting testMeeting;

    @BeforeEach
    void setUp() {
        String meetingId = "6966b86415de9141342edb20";
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

        this.testMeeting = Meeting.builder()
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

        LocalDate updateRequestDate = LocalDate.of(2026, 2, 3);

        Instant updatedMeetingDate = updateRequestDate.
                atStartOfDay(ZoneId.systemDefault()).toInstant();

        Instant updatedStartTime = updateRequestDate
                .atTime(14, 30)
                .atZone(ZoneId.systemDefault()).toInstant();

        Instant updatedEndTIme = updateRequestDate
                .atTime(15, 0)
                .atZone(ZoneId.systemDefault()).toInstant();

        this.updateRequest = UpdateRequest.builder()
                .date(updatedMeetingDate)
                .startTime(updatedStartTime)
                .endTime(updatedEndTIme)
                .status(MeetingStatus.CONFIRMED)
                .build();


    }

    @Test
    void bookMeetingShouldCreateMeeting() {

        when(doctorClient.getDoctorById(bookingRequest.getDoctorId()))
                .thenReturn(doctorResponseDTO);
        when(patientClient.getPatientById(bookingRequest.getPatientId()))
                .thenReturn(patientResponseDTO);

        when(meetingRepository.save(any(Meeting.class))).thenReturn(testMeeting);

        MeetingResponse response = meetingService.createMeeting(bookingRequest);

        Assertions.assertNotNull(response);

        verify(doctorClient).getDoctorById(bookingRequest.getDoctorId());
        verify(patientClient).getPatientById(bookingRequest.getPatientId());
    }

    @Test
    void bookMeetingShouldNotCreateIfDoctorStatusIsNotActive() {
        doctorResponseDTO.setDoctorStatus("INACTIVE");

        when(doctorClient.getDoctorById(bookingRequest.getDoctorId()))
                .thenReturn(doctorResponseDTO);
        when(patientClient.getPatientById(bookingRequest.getPatientId()))
                .thenReturn(patientResponseDTO);

        doThrow(InvalidStatusException.class)
                .when(doctorValidation)
                .checkDoctorStatus(doctorResponseDTO);

        Assertions.assertThrows(InvalidStatusException.class, () -> {
            meetingService.createMeeting(bookingRequest);
        });

        verify(meetingRepository, never()).save(any(Meeting.class));
    }

    @Test
    void bookMeetingShouldNotCreateIfPatientStatusIsNotActive() {
        doctorResponseDTO.setDoctorStatus("INACTIVE");

        when(doctorClient.getDoctorById(bookingRequest.getDoctorId()))
                .thenReturn(doctorResponseDTO);

        when(patientClient.getPatientById(bookingRequest.getPatientId()))
                .thenReturn(patientResponseDTO);

        doThrow(InvalidStatusException.class)
                .when(patientValidation)
                .checkPatientStatus(patientResponseDTO);

        Assertions.assertThrows(InvalidStatusException.class, () -> {
            meetingService.createMeeting(bookingRequest);
        });

        verify(meetingRepository, never()).save(any(Meeting.class));
    }

    @Test
    void bookMeetingShouldNotCreateIfDoctorScheduleIsEmpty() {
        doctorResponseDTO.setSchedulesList(List.of());

        when(doctorClient.getDoctorById(bookingRequest.getDoctorId()))
                .thenReturn(doctorResponseDTO);

        when(patientClient.getPatientById(bookingRequest.getPatientId()))
                .thenReturn(patientResponseDTO);

        doThrow(ScheduleNotAvailableException.class)
                .when(doctorValidation)
                .checkDoctorSchedule(doctorResponseDTO, bookingRequest.getMeetingStartTime(), bookingRequest.getMeetingEndTime());

        Assertions.assertThrows(ScheduleNotAvailableException.class, () -> {
            meetingService.createMeeting(bookingRequest);
        });

        verify(meetingRepository, never()).save(any(Meeting.class));
    }

    @Test
    void bookMeetingShouldNotCreateIfScheduleConflictOccurs() {
        bookingRequest.setMeetingStartTime(Instant.parse("2026-02-01T08:00:00Z"));
        bookingRequest.setMeetingEndTime(Instant.parse("2026-02-01T09:00:00Z"));

        when(doctorClient.getDoctorById(bookingRequest.getDoctorId()))
                .thenReturn(doctorResponseDTO);

        when(patientClient.getPatientById(bookingRequest.getPatientId()))
                .thenReturn(patientResponseDTO);

        doThrow(ScheduleNotAvailableException.class)
                .when(doctorValidation)
                .checkDoctorSchedule(
                        doctorResponseDTO, bookingRequest.getMeetingStartTime(), bookingRequest.getMeetingEndTime());

        Assertions.assertThrows(ScheduleNotAvailableException.class, () -> {
            meetingService.createMeeting(bookingRequest);
        });

        verify(meetingRepository, never()).save(any(Meeting.class));
    }

    @Test
    void bookMeetingShouldNotCreateIfMeetingStartTimeIsBetweenBreakTime() {
        bookingRequest.setMeetingStartTime(Instant.parse("2026-02-01T12:00:00Z"));
        bookingRequest.setMeetingEndTime(Instant.parse("2026-02-01T13:00:00Z"));

        when(doctorClient.getDoctorById(bookingRequest.getDoctorId()))
                .thenReturn(doctorResponseDTO);

        when(patientClient.getPatientById(bookingRequest.getPatientId()))
                .thenReturn(patientResponseDTO);

        doNothing().when(doctorValidation).checkDoctorStatus(doctorResponseDTO);
        doNothing().when(patientValidation).checkPatientStatus(patientResponseDTO);

        doThrow(InvalidMeetingException.class)
                .when(doctorValidation)
                .checkDoctorSchedule(doctorResponseDTO, bookingRequest.getMeetingStartTime(), bookingRequest.getMeetingEndTime());

        Assertions.assertThrows(InvalidMeetingException.class, () -> {
            meetingService.createMeeting(bookingRequest);
        });

        verify(meetingRepository, never()).save(any(Meeting.class));
    }

    @Test
    void bookMeetingShouldNotCreateIfDoctorIsNotAvailable() {
        bookingRequest.setMeetingStartTime(Instant.parse("2026-02-01T09:30:00Z"));
        bookingRequest.setMeetingEndTime(Instant.parse("2026-02-01T10:30:00Z"));

        doNothing().when(doctorValidation).checkDoctorStatus(doctorResponseDTO);
        doNothing().when(patientValidation).checkPatientStatus(patientResponseDTO);
        doNothing().when(doctorValidation).checkDoctorSchedule(
                doctorResponseDTO, bookingRequest.getMeetingStartTime(), bookingRequest.getMeetingEndTime()
        );

        doThrow(MeetingConflictException.class)
                .when(doctorValidation)
                .checkDoctorAvailability(
                        bookingRequest.getDoctorId(), bookingRequest.getMeetingStartTime(), bookingRequest.getMeetingEndTime());

        Assertions.assertThrows(MeetingConflictException.class, () -> {
            meetingService.createMeeting(bookingRequest);
        });

        verify(meetingRepository, never()).save(any(Meeting.class));
    }

    @Test
    void getMeetingByPatientIdShouldReturnMeeting() {
        UUID patientId = UUID.fromString(patientResponseDTO.getId());

        when(meetingRepository.findByPatientId(patientId)).thenReturn(List.of(testMeeting));

        List<MeetingResponse> listOfMeetingResponses = meetingService.findByPatientId(patientId);
        MeetingResponse testResponse = listOfMeetingResponses.getFirst();

        Assertions.assertNotNull(listOfMeetingResponses);
        Assertions.assertEquals(testResponse.getPatient().getId(), patientId.toString());
        Assertions.assertEquals(testResponse.getPatient().getFirstName(), patientResponseDTO.getFirstName());
        Assertions.assertEquals(testResponse.getPatient().getLastName(), patientResponseDTO.getLastName());
    }

    @Test
    void getMeetingByPatientIdShouldReturnEmptyListWhenNoMeetingIsFound() {
        UUID patientId = UUID.fromString(patientResponseDTO.getId());

        when(meetingRepository.findByPatientId(patientId)).thenReturn(List.of());

        verify(meetingRepository, never()).findByPatientId(patientId);
    }

    @Test
    void getMeetingByDoctorIdShouldReturnMeeting() {
        UUID doctorId = UUID.fromString(doctorResponseDTO.getId());

        when(meetingRepository.findByDoctorId(doctorId)).thenReturn(List.of(testMeeting));

        List<MeetingResponse> listOfMeetingResponses = meetingService.findByDoctorId(doctorId);
        MeetingResponse testResponse = listOfMeetingResponses.getFirst();

        Assertions.assertNotNull(listOfMeetingResponses);
        Assertions.assertEquals(testResponse.getDoctor().getId(), doctorId.toString());
        Assertions.assertEquals(testResponse.getDoctor().getFirstName(), doctorResponseDTO.getFirstName());
        Assertions.assertEquals(testResponse.getDoctor().getLastName(), doctorResponseDTO.getLastName());
    }

    @Test
    void getMeetingByDoctorIdShouldReturnEmptyListWhenNoMeetingIsFound() {
        UUID doctorId = UUID.fromString(doctorResponseDTO.getId());

        when(meetingRepository.findByDoctorId(doctorId)).thenReturn(List.of());

        verify(meetingRepository, never()).findByDoctorId(doctorId);
    }

    @Test
    void getMeetingByStatusShouldReturnMeeting() {
        MeetingStatus status = MeetingStatus.CONFIRMED;

        when(meetingRepository.findByStatus(status)).thenReturn(List.of(testMeeting));

        List<MeetingResponse> listOfMeetingResponses = meetingService.findByStatus(status);
        MeetingResponse testResponse = listOfMeetingResponses.getFirst();

        Assertions.assertNotNull(listOfMeetingResponses);
        Assertions.assertEquals(testResponse.getMeetingStatus(),  status.toString());
    }

    @Test
    void getMeetingByStatusShouldReturnEmptyListWhenNoMeetingIsFound() {
        MeetingStatus status = MeetingStatus.COMPLETED;

        when(meetingRepository.findByStatus(status)).thenReturn(List.of());

        verify(meetingRepository, never()).findByStatus(status);
    }

    @Test
    void updateMeetingShouldSuccessfullyUpdateMeeting() {
        String meetingId = testMeeting.getId();

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(testMeeting));
        when(doctorClient.getDoctorById(testMeeting.getDoctorId().toString()))
                .thenReturn(doctorResponseDTO);

        when(meetingRepository.save(any(Meeting.class))).thenReturn(testMeeting);

        MeetingResponse meetingResponse = meetingService.updateMeeting(meetingId, updateRequest);

        Assertions.assertNotNull(meetingResponse);

        verify(meetingRepository).save(any(Meeting.class));
    }

    @Test
    void updateMeetingShouldNotUpdateIfMeetingIsNotFound() {
        String meetingId = "";

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(MeetingNotFoundException.class, () ->
                meetingService.updateMeeting(meetingId, updateRequest));

        verify(meetingRepository).findById(meetingId);
        verify(meetingRepository, never()).save(any(Meeting.class));
    }

    @Test
    void updateMeetingShouldNotUpdateIfMeetingStatusIsCancelled() {
        String meetingId = testMeeting.getId();

        updateRequest.setStatus(MeetingStatus.CANCELLED);

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(testMeeting));

        Assertions.assertThrows(MeetingConflictException.class, () ->
                meetingService.updateMeeting(meetingId, updateRequest));

        verify(meetingRepository).findById(meetingId);
        verify(meetingRepository, never()).save(any(Meeting.class));
    }

    @Test
    void cancelMeetingShouldCancelMeetingSuccessfully() {
        String meetingId = testMeeting.getId();

        testMeeting.setStatus(MeetingStatus.CONFIRMED);

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(testMeeting));

        meetingService.cancelMeeting(meetingId);

        when(meetingRepository.save(any(Meeting.class))).thenReturn(testMeeting);

        Assertions.assertEquals(testMeeting.getStatus(), MeetingStatus.CANCELLED);

        verify(meetingRepository).save(any(Meeting.class));
    }

    @Test
    void cancelMeetingShouldNotCancelIfMeetingIsNotFound() {
        String meetingId = "";

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(MeetingNotFoundException.class, () ->
                meetingService.cancelMeeting(meetingId));

        verify(meetingRepository).findById(meetingId);
        verify(meetingRepository, never()).save(any(Meeting.class));
    }

    @Test
    void cancelMeetingShouldNotCancelIfStatusIsAlreadyCancelled() {
        String meetingId = testMeeting.getId();

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(testMeeting));

        testMeeting.setStatus(MeetingStatus.CANCELLED);

        Assertions.assertThrows(MeetingConflictException.class, () ->
                meetingService.cancelMeeting(meetingId));

        verify(meetingRepository).findById(meetingId);
        verify(meetingRepository, never()).save(any(Meeting.class));
    }
}