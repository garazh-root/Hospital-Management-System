package org.com.meetingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.meetingservice.dto.AvailableSlotResponse;
import org.com.meetingservice.dto.MeetingResponse;
import org.com.meetingservice.requests.MeetingRequest;
import org.com.meetingservice.service.MeetingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MeetingController.class)
public class MeetingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MeetingService meetingService;

    private UUID doctorId;
    private UUID patientId;

    @BeforeEach
    void setUp() {
        doctorId = UUID.randomUUID();
        patientId = UUID.randomUUID();
    }

    @Test
    void getAvailableSlotsShouldFetchAndReturnAvailableSlots() throws Exception {
        List<AvailableSlotResponse> availableSlotResponseList = List.of(
                new AvailableSlotResponse("2026-06-01", "09:00", "09:30", "30"),
                new AvailableSlotResponse("2026-06-01", "09:30", "10:00", "30")
        );

        when(meetingService.getAvailableSlots(doctorId.toString(), LocalDate.of(2026, 6, 01))).thenReturn(availableSlotResponseList);

        mockMvc.perform(get("/meeting/slots/{doctorId}", doctorId)
                        .param("date", "2026-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(availableSlotResponseList.size()))
                .andExpect(jsonPath("[0].startTime").value("09:00"))
                .andExpect(jsonPath("[1].startTime").value("09:30"));
    }

    @Test
    void getAvailableSlotsShouldReturnEmptyList() throws Exception {
        when(meetingService.getAvailableSlots(anyString(), any(LocalDate.class))).thenReturn(List.of());

        mockMvc.perform(get("/meeting/slots/{doctorId}", doctorId)
                        .param("date", "2026-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void bookMeetingShouldReturn200WithMeetingResponse() throws Exception {
        MeetingRequest request = new MeetingRequest(
                doctorId, patientId,
                LocalDateTime.of(2026, 6, 1, 9, 0),
                30, "Checkup"
        );

        MeetingResponse meetingResponse = new MeetingResponse(
                "2dce8bcd90fd18bea8f4ff7b", doctorId.toString(), patientId.toString(),
                "2026-06-01T09:00", "30", "CONFIRMED", "test"
        );

        when(meetingService.bookMeeting(any(MeetingRequest.class))).thenReturn(meetingResponse);

        mockMvc.perform(post("/meeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.doctorId").value(doctorId.toString()))
                .andExpect(jsonPath("$.patientId").value(patientId.toString()));
    }

    @Test
    void bookMeetingShouldReturn500StatusWhenSlotUnavailable() throws Exception {
        MeetingRequest request = new MeetingRequest(
                doctorId, patientId,
                LocalDateTime.of(2026, 6, 1, 9, 0),
                30, "Checkup"
        );

        when(meetingService.bookMeeting(any(MeetingRequest.class))).thenThrow(new IllegalStateException("Slot is unavailable"));

        mockMvc.perform(post("/meeting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void findByDoctorIdAndDateTimeBetweenShouldReturn200WithMeetingResponse() throws Exception {
        MeetingResponse meetingResponse = new MeetingResponse(
                "2dce8bcd90fd18bea8f4ff7b", doctorId.toString(), patientId.toString(),
                "2026-06-05T09:00", "30", "CONFIRMED", "test"
        );

        when(meetingService.findByDoctorIdAndDateTimeBetween(eq(doctorId), any(), any()))
                .thenReturn(List.of(meetingResponse));

        mockMvc.perform(get("/meeting/filterByDoctorIdAndDateBetween/{doctorId}", doctorId)
                        .param("startDate", "2026-06-01")
                        .param("endDate", "2026-06-30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(List.of(meetingResponse).size()))
                .andExpect(jsonPath("$[0].doctorId").value(doctorId.toString()));
    }

    @Test
    void findByDoctorIdAndDateTimeBetweenAndStatusShouldReturn200WithAvailableSlots() throws Exception {
        MeetingResponse meetingResponse = new MeetingResponse(
                "2dce8bcd90fd18bea8f4ff7b", doctorId.toString(), patientId.toString(),
                "2026-06-05T09:00", "30", "CONFIRMED", "test"
        );

        when(meetingService.findByDoctorIdAndDateTimeBetweenAndStatus(eq(doctorId), any(), any(), any()))
                .thenReturn(List.of(meetingResponse));

        mockMvc.perform(get("/meeting/filterByDoctorIdAndDateBetweenAndStatus/{doctorId}", doctorId)
                        .param("startDate", "2026-06-01")
                        .param("endDate", "2026-06-30")
                        .param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(List.of(meetingResponse).size()))
                .andExpect(jsonPath("$[0].doctorId").value(doctorId.toString()))
                .andExpect(jsonPath("$[0].patientId").value(patientId.toString()));
    }

    @Test
    void findByPatientIdAndDateTimeBetweenShouldReturn200WithAvailableSlots() throws Exception {
        MeetingResponse meetingResponse = new MeetingResponse(
                "2dce8bcd90fd18bea8f4ff7b", doctorId.toString(), patientId.toString(),
                "2026-06-05T09:00", "30", "CONFIRMED", "test"
        );

        when(meetingService.findByPatientId(eq(patientId))).thenReturn(List.of(meetingResponse));

        mockMvc.perform(get("/meeting/filterByPatientId/{patientId}", patientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(List.of(meetingResponse).size()))
                .andExpect(jsonPath("$[0].doctorId").value(doctorId.toString()))
                .andExpect(jsonPath("$[0].patientId").value(patientId.toString()));
    }

    @Test
    void cancelMeetingShouldReturn204() throws Exception {
        doNothing().when(meetingService).cancelMeeting("0610d7278d22fab21430f4c8");

        mockMvc.perform(delete("/meeting/{meetingId}", "0610d7278d22fab21430f4c8"))
                .andExpect(status().isNoContent());

        verify(meetingService).cancelMeeting("0610d7278d22fab21430f4c8");
    }
}