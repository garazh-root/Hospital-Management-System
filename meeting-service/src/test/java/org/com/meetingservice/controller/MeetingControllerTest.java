package org.com.meetingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.dto.MeetingResponse;
import org.com.meetingservice.info.DoctorInfo;
import org.com.meetingservice.info.PatientInfo;
import org.com.meetingservice.requests.BookingRequest;
import org.com.meetingservice.requests.UpdateRequest;
import org.com.meetingservice.service.MeetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(MeetingController.class)
public class MeetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MeetingService meetingService;

    private MeetingResponse response;
    private DoctorInfo doctorInfo;
    private PatientInfo patientInfo;
    private String meetingId;
    private UUID patientId;
    private UUID doctorId;
    private BookingRequest bookingRequest;
    private UpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        meetingId = "5a7fc357bf6df4ad8ffe3d2a";
        patientId = UUID.randomUUID();
        doctorId = UUID.randomUUID();

        doctorInfo = DoctorInfo.builder()
                .id(doctorId.toString())
                .firstName("King")
                .lastName("Royal")
                .specialization("Dentist")
                .build();

        patientInfo = PatientInfo.builder()
                .id(patientId.toString())
                .firstName("Gem")
                .lastName("Jewels")
                .build();

        response = MeetingResponse.builder()
                .id(meetingId)
                .meetingDate("2026-05-15")
                .startTime("10:00:00")
                .endTime("11:00:00")
                .meetingStatus(MeetingStatus.CONFIRMED.toString())
                .notes("Regular test")
                .patient(patientInfo)
                .doctor(doctorInfo)
                .createdAt("2026-05-15T09:00:00Z")
                .updatedAt("2024-05-15T09:00:00Z")
                .build();

        bookingRequest = BookingRequest.builder()
                .doctorId(doctorId.toString())
                .patientId(patientId.toString())
                .meetingDate(Instant.parse("2026-05-15T00:00:00Z"))
                .meetingStartTime(Instant.parse("2026-05-15T10:00:00Z"))
                .meetingEndTime(Instant.parse("2026-05-15T11:00:00Z"))
                .notes("Regular checkup")
                .build();

        updateRequest = UpdateRequest.builder()
                .date(Instant.parse("2026-05-21T00:00:00Z"))
                .startTime(Instant.parse("2026-05-21T15:00:00Z"))
                .endTime(Instant.parse("2026-05-21T16:00:00Z"))
                .status(MeetingStatus.CONFIRMED)
                .build();
    }

    @Test
    void getMeetingByIdShouldReturnListOfMeetings() throws Exception {
        List<MeetingResponse> meetings = List.of(response);

        when(meetingService.findByPatientId(patientId)).thenReturn(meetings);

        mockMvc.perform(get("/meeting/filterByPatientId")
                .param("patientId", patientId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(meetingId)))
                .andExpect(jsonPath("$[0].patient.id", is(patientId.toString())))
                .andExpect(jsonPath("$[0].doctor.id", is(doctorId.toString())))
                .andExpect(jsonPath("$[0].meetingStatus", is(MeetingStatus.CONFIRMED.toString())))
                .andExpect(jsonPath("$[0].notes", is("Regular test")));

        verify(meetingService, times(1)).findByPatientId(patientId);
    }

    @Test
    void getMeetingByIdShouldReturnListOfEmptyListIfMeetingNotFound() throws Exception {
        List<MeetingResponse> meetings = Collections.emptyList();

        when(meetingService.findByPatientId(patientId)).thenReturn(meetings);

        mockMvc.perform(get("/meeting/filterByPatientId")
                        .param("patientId", patientId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(meetingService, times(1)).findByPatientId(patientId);
    }

    @Test
    void getMeetingByDoctorIdShouldReturnListOfMeetings() throws Exception {
        List<MeetingResponse> meetings = List.of(response);

        when(meetingService.findByDoctorId(doctorId)).thenReturn(meetings);

        mockMvc.perform(get("/meeting/filterByDoctorId")
                .param("doctorId", doctorId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(meetingId)))
                .andExpect(jsonPath("$[0].doctor.id", is(doctorId.toString())))
                .andExpect(jsonPath("$[0].doctor.firstName", is("King")))
                .andExpect(jsonPath("$[0].doctor.lastName", is("Royal")))
                .andExpect(jsonPath("$[0].doctor.specialization", is("Dentist")));

        verify(meetingService, times(1)).findByDoctorId(doctorId);
    }

    @Test
    void getMeetingByDoctorIdShouldReturnListOfEmptyListIfDoctorNotFound() throws Exception {
        List<MeetingResponse> meetings = Collections.emptyList();

        when(meetingService.findByDoctorId(doctorId)).thenReturn(meetings);

        mockMvc.perform(get("/meeting/filterByDoctorId")
                        .param("doctorId", doctorId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(meetingService, times(1)).findByDoctorId(doctorId);
    }

    @Test
    void getMeetingByStatusShouldReturnListOfMeetings() throws Exception {
        List<MeetingResponse> meetings = List.of(response);

        when(meetingService.findByStatus(MeetingStatus.CONFIRMED)).thenReturn(meetings);

        mockMvc.perform(get("/meeting/filterByStatus")
                .param("status", MeetingStatus.CONFIRMED.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(meetingId)))
                .andExpect(jsonPath("$[0].meetingStatus", is(MeetingStatus.CONFIRMED.toString())));

        verify(meetingService, times(1)).findByStatus(MeetingStatus.CONFIRMED);
    }

    @Test
    void getMeetingByStatusShouldReturnEmptyListIfMeetingNotFound() throws Exception {
        List<MeetingResponse> meetings = Collections.emptyList();

        when(meetingService.findByStatus(MeetingStatus.CONFIRMED)).thenReturn(meetings);

        mockMvc.perform(get("/meeting/filterByStatus")
                        .param("status", MeetingStatus.CONFIRMED.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(meetingService, times(1)).findByStatus(MeetingStatus.CONFIRMED);
    }

    @Test
    void createMeetingShouldSuccessfullyBookMeeting() throws Exception {
        when(meetingService.createMeeting(any(BookingRequest.class))).thenReturn(response);

        mockMvc.perform(post("/meeting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(meetingId)))
                .andExpect(jsonPath("$.meetingDate", is("2026-05-15")))
                .andExpect(jsonPath("$.startTime", is("10:00:00")))
                .andExpect(jsonPath("$.endTime", is("11:00:00")))
                .andExpect(jsonPath("$.meetingStatus", is(MeetingStatus.CONFIRMED.toString())))
                .andExpect(jsonPath("$.notes", is("Regular test")))
                .andExpect(jsonPath("$.doctor.id", is(doctorId.toString())))
                .andExpect(jsonPath("$.patient.id", is(patientId.toString())));

        verify(meetingService, times(1)).createMeeting(any(BookingRequest.class));
    }

    @Test
    void createMeetingShouldReturnBadRequestWhenValidationFails() throws Exception {
        BookingRequest bookingRequest = BookingRequest.builder().build();

        mockMvc.perform(post("/meeting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bookingRequest)))
                .andExpect(status().isBadRequest());

        verify(meetingService, never()).createMeeting(any(BookingRequest.class));
    }

    @Test
    void updateMeetingShouldSuccessfullyUpdateMeeting() throws Exception {
        MeetingResponse meetingResponse = MeetingResponse.builder()
                .id(meetingId)
                .meetingDate("2026-05-15")
                .startTime("14:00:00")
                .endTime("15:00:00")
                .meetingStatus(MeetingStatus.CONFIRMED.toString())
                .notes("Regular test")
                .patient(patientInfo)
                .doctor(doctorInfo)
                .createdAt("2026-05-15T09:00:00Z")
                .updatedAt("2026-05-15T09:00:00Z")
                .build();

        when(meetingService.updateMeeting(eq(meetingId.toString()), any(UpdateRequest.class)))
                .thenReturn(meetingResponse);

        mockMvc.perform(put("/meeting/" + meetingId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(meetingId)))
                .andExpect(jsonPath("$.meetingDate", is("2026-05-15")))
                .andExpect(jsonPath("$.startTime", is("14:00:00")))
                .andExpect(jsonPath("$.endTime", is("15:00:00")));

        verify(meetingService, times(1)).updateMeeting(eq(meetingId.toString()), any(UpdateRequest.class));
    }

    @Test
    void updateMeetingShouldReturnBadRequestWhenValidationFails() throws Exception {
        UpdateRequest updateRequest = UpdateRequest.builder().build();

        mockMvc.perform(put("/meeting/" + meetingId.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isBadRequest());

        verify(meetingService, never()).updateMeeting(eq(meetingId.toString()), any(UpdateRequest.class));
    }

    @Test
    void cancelMeetingShouldSuccessfullyCancelMeeting() throws Exception {
        doNothing().when(meetingService).cancelMeeting(eq(meetingId.toString()));

        mockMvc.perform(patch("/meeting/" + meetingId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(meetingService, times(1)).cancelMeeting(eq(meetingId.toString()));
    }
}