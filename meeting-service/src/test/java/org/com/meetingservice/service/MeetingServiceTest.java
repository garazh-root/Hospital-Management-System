package org.com.meetingservice.service;

import io.swagger.v3.oas.models.security.SecurityScheme;
import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.client.DoctorClient;
import org.com.meetingservice.dto.AvailableSlotResponse;
import org.com.meetingservice.dto.MeetingResponse;
import org.com.meetingservice.dto.ScheduleResponse;
import org.com.meetingservice.kafka.KafkaProducer;
import org.com.meetingservice.model.Meeting;
import org.com.meetingservice.repository.MeetingRepository;
import org.com.meetingservice.requests.MeetingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
public class MeetingServiceTest {

    @Mock
    private MeetingRepository meetingRepository;

    @Mock
    private SlotGeneratorService slotGeneratorService;

    @Mock
    private DoctorClient doctorClient;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private MeetingService meetingService;

    private UUID doctorId;
    private UUID patientId;
    private Instant date;
    private ScheduleResponse scheduleResponse;

    @BeforeEach
    void setUp() {
        doctorId = UUID.randomUUID();
        patientId = UUID.randomUUID();
        date = LocalDateTime.of(2026, 3, 26, 9, 0, 0).atZone(ZoneId.of("UTC")).toInstant();
        scheduleResponse = mock(ScheduleResponse.class);
    }

    @Test
    void getAvailableSlotsShouldReturnSlotsFromGeneratorService() {
        List<AvailableSlotResponse> list = List.of(
                new AvailableSlotResponse("2026-03-26", "09:00", "09:30", "30"),
                new AvailableSlotResponse("2026-03-26", "09:30", "10:00", "30")
        );

        when(doctorClient.getSchedulesData(doctorId.toString(), date.atZone(ZoneId.of("UTC")).toLocalDate(), date.atZone(ZoneId.of("UTC")).toLocalDate())).thenReturn(scheduleResponse);
        when(meetingRepository.findScheduledMeetingsForDate(eq(doctorId), any(Instant.class), any(Instant.class)))
                .thenReturn(Collections.emptyList());
        when(slotGeneratorService.generateAvailableSlots(any(ScheduleResponse.class), eq(date.atZone(ZoneId.of("UTC")).toLocalDate()), anyList()))
                .thenReturn(list);

        List<AvailableSlotResponse> availableSlotResponses = meetingService.getAvailableSlots(doctorId.toString(), date.atZone(ZoneId.of("UTC")).toLocalDate());

        assertThat(availableSlotResponses.size()).isEqualTo(list.size());
        verify(doctorClient).getSchedulesData(doctorId.toString(), date.atZone(ZoneId.of("UTC")).toLocalDate(), date.atZone(ZoneId.of("UTC")).toLocalDate());
    }

    @Test
    void getAvailableSlotsShouldReturnSlotsFromGeneratorServiceAndAlsoBookedMeetings() {
        Meeting meeting = Meeting.builder()
                .doctorId(doctorId)
                .patientId(patientId)
                .meetingDateTime(Instant.from(date))
                .durationOfMinutes(30)
                .status(MeetingStatus.CONFIRMED)
                .reason("Regular checkup")
                .build();

        when(doctorClient.getSchedulesData(doctorId.toString(), date.atZone(ZoneId.of("UTC")).toLocalDate(), date.atZone(ZoneId.of("UTC")).toLocalDate())).thenReturn(scheduleResponse);
        when(meetingRepository.findScheduledMeetingsForDate(any(UUID.class), any(Instant.class), any(Instant.class)))
                .thenReturn(List.of(meeting));
        when(slotGeneratorService.generateAvailableSlots(any(ScheduleResponse.class), eq(date.atZone(ZoneId.of("UTC")).toLocalDate()), anyList()))
                .thenReturn(Collections.emptyList());

        meetingService.getAvailableSlots(doctorId.toString(), date.atZone(ZoneId.of("UTC")).toLocalDate());

        ArgumentCaptor<List<Instant>> captor = ArgumentCaptor.forClass(List.class);
        verify(slotGeneratorService).generateAvailableSlots(any(ScheduleResponse.class), eq(date.atZone(ZoneId.of("UTC")).toLocalDate()), captor.capture());
        assertThat(captor.getValue()).containsExactly(date);
    }

    @Test
    void bookMeetingShouldSaveMeetingAndReturnMeetingResponse() {
        LocalDateTime dateTime = LocalDateTime.of(date.atZone(ZoneId.of("UTC")).toLocalDate(), LocalTime.of(10, 0));
        MeetingRequest request = new MeetingRequest(doctorId, patientId, dateTime, 30, "Regular checkup");

        AvailableSlotResponse slotResponse = new AvailableSlotResponse("2026-03-26", "10:00", "10:30", "30");

        when(doctorClient.getSchedulesData(doctorId.toString(), date.atZone(ZoneId.of("UTC")).toLocalDate(), date.atZone(ZoneId.of("UTC")).toLocalDate())).thenReturn(scheduleResponse);
        when(meetingRepository.findScheduledMeetingsForDate(any(UUID.class), any(Instant.class), any(Instant.class)))
                .thenReturn(Collections.emptyList());
        when(slotGeneratorService.generateAvailableSlots(any(ScheduleResponse.class), eq(date.atZone(ZoneId.of("UTC")).toLocalDate()), anyList()))
                .thenReturn(List.of(slotResponse));

        ArgumentCaptor<Meeting> argumentCaptor = ArgumentCaptor.forClass(Meeting.class);

        when(meetingRepository.save(argumentCaptor.capture())).thenAnswer(i -> i.getArgument(0));

        MeetingResponse response = meetingService.bookMeeting(request);

        Meeting meeting = argumentCaptor.getValue();
        assertThat(meeting.getDoctorId()).isEqualTo(doctorId);
        assertThat(meeting.getPatientId()).isEqualTo(patientId);
    }

    @Test
    void bookMeetingShouldThrowExceptionWhenSlotIsNotAvailable() {
        LocalDateTime dateTime = LocalDateTime.of(date.atZone(ZoneId.of("UTC")).toLocalDate(), LocalTime.of(10, 0));
        MeetingRequest request = new MeetingRequest(doctorId, patientId, dateTime, 30, "Regular checkup");

        AvailableSlotResponse slotResponse = new AvailableSlotResponse("2026-03-26", "08:30", "09:00", "30");

        when(doctorClient.getSchedulesData(doctorId.toString(), date.atZone(ZoneId.of("UTC")).toLocalDate(), date.atZone(ZoneId.of("UTC")).toLocalDate())).thenReturn(scheduleResponse);
        when(meetingRepository.findScheduledMeetingsForDate(any(UUID.class), any(Instant.class), any(Instant.class)))
                .thenReturn(Collections.emptyList());
        when(slotGeneratorService.generateAvailableSlots(any(ScheduleResponse.class), eq(date.atZone(ZoneId.of("UTC")).toLocalDate()), anyList()))
                .thenReturn(List.of(slotResponse));

        assertThatThrownBy(() -> meetingService.bookMeeting(request)).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void findMeetingByDoctorIdAndDateTimeBetweenShouldReturnListOfMeetingMappedResponses() {
        LocalDate startDate = LocalDate.of(2026, 3, 1);
        LocalDate endDate = LocalDate.of(2026, 3, 30);

        Meeting meeting = meetingBuilder(doctorId, patientId, LocalDateTime.of(2026, 3, 15, 9, 0).atZone(ZoneId.of("UTC")).toInstant());

        when(meetingRepository.findByDoctorIdAndMeetingDateTimeBetween(eq(doctorId), any(Instant.class), any(Instant.class)))
                .thenReturn(List.of(meeting));

        List<MeetingResponse> meetingResponseList = meetingService.findByDoctorIdAndDateTimeBetween(doctorId, startDate, endDate);

        assertThat(meetingResponseList.size()).isEqualTo(1);
    }

    @Test
    void indMeetingByDoctorIdAndDateTimeBetweenShouldReturnEmptyListIfNoMeetingFound() {
        when(meetingRepository.findByDoctorIdAndMeetingDateTimeBetween(any(),  any(), any())).thenReturn(Collections.emptyList());

        List<MeetingResponse> responseList = meetingService.findByDoctorIdAndDateTimeBetween(doctorId, LocalDate.now(), LocalDate.now());

        assertThat(responseList).isEmpty();
    }

    @Test
    void findMeetingByDoctorIdAndDateTimeBetweenAndStatusShouldReturnListOfMeetingMappedResponses() {
        LocalDate startDate = LocalDate.of(2026, 3, 1);
        LocalDate endDate = LocalDate.of(2026, 3, 30);

        Meeting meeting = meetingBuilder(doctorId, patientId, LocalDateTime.of(2026, 3, 15, 9, 0).atZone(ZoneId.of("UTC")).toInstant());

        when(meetingRepository.findByDoctorIdAndMeetingDateTimeBetweenAndStatus(eq(doctorId), any(Instant.class), any(Instant.class), eq(MeetingStatus.CONFIRMED)))
                .thenReturn(List.of(meeting));

        List<MeetingResponse> meetingResponseList = meetingService.findByDoctorIdAndDateTimeBetweenAndStatus(doctorId, startDate, endDate, MeetingStatus.CONFIRMED);

        assertThat(meetingResponseList.size()).isEqualTo(1);
    }

    @Test
    void findMeetingByDoctorIdAndDateTimeBetweenAndStatusShouldReturnEmptyListIfNoMeetingFound() {
        when(meetingRepository.findByDoctorIdAndMeetingDateTimeBetweenAndStatus(any(), any(), any(), any())).thenReturn(Collections.emptyList());

        List<MeetingResponse> list = meetingService.findByDoctorIdAndDateTimeBetweenAndStatus(doctorId, LocalDate.now(), LocalDate.now(), MeetingStatus.CONFIRMED);

        assertThat(list).isEmpty();
    }

    @Test
    void findMeetingByPatientIdAndDateTimeBetweenAndStatusShouldReturnListOfMeetingMappedResponses() {
        Meeting meeting = meetingBuilder(doctorId, patientId, Instant.now());
        when(meetingRepository.findByPatientId(patientId)).thenReturn(List.of(meeting));

        List<MeetingResponse> meetingResponseList = meetingService.findByPatientId(patientId);

        assertThat(meetingResponseList.size()).isEqualTo(1);
    }

    @Test
    void findMeetingByPatientIdAndDateTimeBetweenAndStatusShouldReturnEmptyListIfNoMeetingFound() {
        when(meetingRepository.findByPatientId(patientId)).thenReturn(Collections.emptyList());

        List<MeetingResponse> list = meetingService.findByPatientId(patientId);
        assertThat(list).isEmpty();
    }

    @Test
    void cancelMeetingShouldChangeStatusToCancelled() {
        String meetingId = "69a827dbc162bec1fe6f4306";
        Meeting meeting = meetingBuilder(doctorId, patientId, Instant.now());

        when(meetingRepository.findById(meetingId)).thenReturn(Optional.of(meeting));
        when(meetingRepository.save(any())).thenReturn(meeting);

        meetingService.cancelMeeting(meetingId);

        assertThat(meeting.getStatus()).isEqualTo(MeetingStatus.CANCELLED);
    }

    private Meeting meetingBuilder(UUID doctorId, UUID patientId, Instant dateTime) {
        return Meeting.builder()
                .doctorId(doctorId)
                .patientId(patientId)
                .meetingDateTime(dateTime)
                .durationOfMinutes(30)
                .status(MeetingStatus.CONFIRMED)
                .notes("Test")
                .reason("Regular checkup")
                .build();
    }
}