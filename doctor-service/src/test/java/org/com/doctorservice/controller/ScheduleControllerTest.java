package org.com.doctorservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.doctorservice.additional.CustomDayOfTheWeek;
import org.com.doctorservice.additional.OverrideType;
import org.com.doctorservice.dto.*;
import org.com.doctorservice.service.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ScheduleService scheduleService;

    private ScheduleTemplateRequest scheduleTemplateRequest;
    private ScheduleOverrideRequest scheduleOverrideRequest;
    private ScheduleTemplateResponse scheduleTemplateResponse;
    private ScheduleOverrideResponse scheduleOverrideResponse;
    private ScheduleResponse scheduleResponse;

    private UUID scheduleTemplateId;
    private UUID scheduleOverrideId;
    private UUID scheduleTemplateResponseId;
    private UUID scheduleOverrideResponseId;
    private UUID doctorId;

    @BeforeEach
    public void setUp() {
        this.scheduleTemplateId = UUID.randomUUID();
        this.scheduleOverrideId = UUID.randomUUID();
        this.scheduleTemplateResponseId = UUID.randomUUID();
        this.scheduleOverrideResponseId = UUID.randomUUID();
        this.doctorId = UUID.randomUUID();

        this.scheduleTemplateRequest = ScheduleTemplateRequest.builder()
                .dayOfTheWeek(CustomDayOfTheWeek.THURSDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .breakStartTime(LocalTime.of(13, 0))
                .breakEndTime(LocalTime.of(14, 0))
                .slotDuration(30)
                .build();

        this.scheduleOverrideRequest = ScheduleOverrideRequest.builder()
                .date(LocalDate.of(2026, 8, 9))
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(18, 0))
                .overrideType(OverrideType.UNAVAILABLE)
                .slotDurationOfMinutes(30)
                .reason("On vacation")
                .build();

        this.scheduleTemplateResponse = ScheduleTemplateResponse.builder()
                .id(scheduleTemplateResponseId.toString())
                .doctorId(doctorId.toString())
                .dayOfTheWeek(CustomDayOfTheWeek.THURSDAY.toString())
                .startTime(LocalTime.of(9, 0).toString())
                .endTime(LocalTime.of(17, 0).toString())
                .breakStartTime(LocalTime.of(13, 0).toString())
                .breakEndTime(LocalTime.of(14, 0).toString())
                .slotDurationOfMinutes(Integer.valueOf(30).toString())
                .build();

        this.scheduleOverrideResponse = ScheduleOverrideResponse.builder()
                .id(scheduleOverrideResponseId.toString())
                .doctorId(doctorId.toString())
                .date(LocalDate.of(2026, 8, 9).toString())
                .overrideType(OverrideType.UNAVAILABLE.toString())
                .startTime(LocalTime.of(10, 0).toString())
                .endTime(LocalTime.of(18, 0).toString())
                .slotDurationOfMinutes(Integer.valueOf(30).toString())
                .reason("On vacation")
                .build();
    }

    @Test
    void createScheduleTemplateShouldSuccessfullyInvokePostMethod() throws Exception {
        when(scheduleService.createScheduleTemplate(doctorId, scheduleTemplateRequest)).thenReturn(scheduleTemplateResponse);

        mockMvc.perform(post("/schedule/template/{doctorIdToCreateTemplate}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleTemplateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(scheduleTemplateResponseId.toString()))
                .andExpect(jsonPath("$.doctorId").value(doctorId.toString()))
                .andExpect(jsonPath("$.dayOfTheWeek").value(CustomDayOfTheWeek.THURSDAY.toString()));

        verify(scheduleService).createScheduleTemplate(doctorId, scheduleTemplateRequest);
        verify(scheduleService, times(1)).createScheduleTemplate(doctorId, scheduleTemplateRequest);
    }

    @Test
    void updateScheduleTemplateShouldSuccessfullyInvokePutMethod() throws Exception {
        when(scheduleService.updateScheduleTemplate(scheduleTemplateId, scheduleTemplateRequest)).thenReturn(scheduleTemplateResponse);

        mockMvc.perform(put("/schedule/templates/{scheduleTemplateId}", scheduleTemplateId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleTemplateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(scheduleTemplateResponseId.toString()))
                .andExpect(jsonPath("$.doctorId").value(doctorId.toString()))
                .andExpect(jsonPath("$.startTime").value(LocalTime.of(9, 0).toString()))
                .andExpect(jsonPath("$.endTime").value(LocalTime.of(17, 0).toString()));

        verify(scheduleService).updateScheduleTemplate(scheduleTemplateId, scheduleTemplateRequest);
        verify(scheduleService, times(1)).updateScheduleTemplate(scheduleTemplateId, scheduleTemplateRequest);
    }

    @Test
    void deleteScheduleTemplateShouldSuccessfullyInvokeDeleteMethod() throws Exception {
        doNothing().when(scheduleService).deleteScheduleTemplate(scheduleTemplateId);

        mockMvc.perform(delete("/schedule/templates/{scheduleTemplateId}", scheduleTemplateId))
                .andExpect(status().isNoContent());

        verify(scheduleService).deleteScheduleTemplate(scheduleTemplateId);
        verify(scheduleService, times(1)).deleteScheduleTemplate(scheduleTemplateId);
    }

    @Test
    void getScheduleTemplatesByIdShouldSuccessfullyInvokeGetMethod() throws Exception {
        List<ScheduleTemplateResponse> scheduleTemplateResponseList = List.of(scheduleTemplateResponse);

        when(scheduleService.getScheduleTemplates(doctorId)).thenReturn(scheduleTemplateResponseList);

        mockMvc.perform(get("/schedule/templates/{doctorId}", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(scheduleTemplateResponseId.toString()))
                .andExpect(jsonPath("$[0].doctorId").value(doctorId.toString()));

        verify(scheduleService).getScheduleTemplates(doctorId);
        verify(scheduleService, times(1)).getScheduleTemplates(doctorId);
    }

    @Test
    void getSchedulesTemplatesByDoctorIdShouldReturnEmptyListIfNoSchedulesExist() throws Exception {
        when(scheduleService.getScheduleTemplates(doctorId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/schedule/templates/{doctorId}", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(scheduleService).getScheduleTemplates(doctorId);
        verify(scheduleService, times(1)).getScheduleTemplates(doctorId);
    }

    @Test
    void getSchedulesByDayShouldSuccessfullyInvokeGetMethod() throws Exception {
        List<ScheduleTemplateResponse> scheduleTemplateResponseList = List.of(scheduleTemplateResponse);

        when(scheduleService.getSchedulesByDoctorIdDayOfTheWeek(doctorId, CustomDayOfTheWeek.THURSDAY)).thenReturn(scheduleTemplateResponseList);

        mockMvc.perform(get("/schedule/templates/byDay/{doctorId}", doctorId)
                        .param("customDayOfTheWeek", CustomDayOfTheWeek.THURSDAY.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(scheduleTemplateResponseId.toString()))
                .andExpect(jsonPath("$[0].doctorId").value(doctorId.toString()));

        verify(scheduleService).getSchedulesByDoctorIdDayOfTheWeek(doctorId, CustomDayOfTheWeek.THURSDAY);
        verify(scheduleService, times(1)).getSchedulesByDoctorIdDayOfTheWeek(doctorId, CustomDayOfTheWeek.THURSDAY);
    }

    @Test
    void getSchedulesByDayShouldReturnEmptyListIfNoSchedulesExist() throws Exception {
        when(scheduleService.getSchedulesByDoctorIdDayOfTheWeek(doctorId, CustomDayOfTheWeek.THURSDAY)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/schedule/templates/byDay/{doctorId}", doctorId)
                        .param("customDayOfTheWeek", CustomDayOfTheWeek.THURSDAY.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(scheduleService).getSchedulesByDoctorIdDayOfTheWeek(doctorId, CustomDayOfTheWeek.THURSDAY);
        verify(scheduleService, times(1)).getSchedulesByDoctorIdDayOfTheWeek(doctorId, CustomDayOfTheWeek.THURSDAY);
    }

    @Test
    void createScheduleOverrideShouldSuccessfullyInvokePostMethod() throws Exception {
        when(scheduleService.createScheduleOverride(doctorId, scheduleOverrideRequest)).thenReturn(scheduleOverrideResponse);

        mockMvc.perform(post("/schedule/override/{doctorId}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scheduleOverrideRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(scheduleOverrideResponseId.toString()))
                .andExpect(jsonPath("$.doctorId").value(doctorId.toString()))
                .andExpect(jsonPath("$.startTime").value(LocalTime.of(10, 0).toString()))
                .andExpect(jsonPath("$.endTime").value(LocalTime.of(18, 0).toString()));

        verify(scheduleService).createScheduleOverride(doctorId, scheduleOverrideRequest);
        verify(scheduleService, times(1)).createScheduleOverride(doctorId, scheduleOverrideRequest);
    }

    @Test
    void deleteScheduleOverrideShouldSuccessfullyInvokeDeleteMethod() throws Exception {
        doNothing().when(scheduleService).deleteScheduleOverride(scheduleOverrideId);

        mockMvc.perform(delete("/schedule/override/{overrideId}", scheduleOverrideId))
                .andExpect(status().isNoContent());

        verify(scheduleService).deleteScheduleOverride(scheduleOverrideId);
        verify(scheduleService, times(1)).deleteScheduleOverride(scheduleOverrideId);
    }

    @Test
    void getSchedulesOverridesByDateShouldSuccessfullyInvokeGetMethod() throws Exception {
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 12, 31);

        List<ScheduleOverrideResponse> scheduleOverrideResponseList = List.of(scheduleOverrideResponse);

        when(scheduleService.getScheduleOverridesByDoctorIdAndDateBetween(doctorId, startDate, endDate)).thenReturn(scheduleOverrideResponseList);

        mockMvc.perform(get("/schedule/overrides/range/{doctorId}", doctorId)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(scheduleOverrideResponseId.toString()))
                .andExpect(jsonPath("$[0].doctorId").value(doctorId.toString()));

        verify(scheduleService).getScheduleOverridesByDoctorIdAndDateBetween(doctorId, startDate, endDate);
        verify(scheduleService, times(1)).getScheduleOverridesByDoctorIdAndDateBetween(doctorId, startDate, endDate);
    }

    @Test
    void getSchedulesOverridesByDateShouldReturnEmptyListIfNoSchedulesExist() throws Exception {
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 12, 31);

        when(scheduleService.getScheduleOverridesByDoctorIdAndDateBetween(doctorId, startDate, endDate)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/schedule/overrides/range/{doctorId}", doctorId)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(scheduleService).getScheduleOverridesByDoctorIdAndDateBetween(doctorId, startDate, endDate);
        verify(scheduleService, times(1)).getScheduleOverridesByDoctorIdAndDateBetween(doctorId, startDate, endDate);
    }

    @Test
    void getSchedulesOverridesByDoctorIdShouldSuccessfullyInvokeGetMethod() throws Exception {
        List<ScheduleOverrideResponse> scheduleOverrideResponseList = List.of(scheduleOverrideResponse);

        when(scheduleService.getScheduleOverridesByDoctorId(doctorId)).thenReturn(scheduleOverrideResponseList);

        mockMvc.perform(get("/schedule/overrides/{doctorId}", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(scheduleOverrideResponseId.toString()))
                .andExpect(jsonPath("$[0].doctorId").value(doctorId.toString()));

        verify(scheduleService).getScheduleOverridesByDoctorId(doctorId);
        verify(scheduleService, times(1)).getScheduleOverridesByDoctorId(doctorId);
    }

    @Test
    void getSchedulesOverridesByDoctorIdShouldReturnEmptyListIfNoSchedulesExist() throws Exception {
        when(scheduleService.getScheduleOverridesByDoctorId(doctorId)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/schedule/overrides/{doctorId}", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(scheduleService).getScheduleOverridesByDoctorId(doctorId);
        verify(scheduleService, times(1)).getScheduleOverridesByDoctorId(doctorId);
    }

    @Test
    void getSchedulesDataByDoctorIdAndDateBetweenShouldSuccessfullyInvokeGetMethod() throws Exception {
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 12, 31);

        ScheduleResponse scheduleResponse = ScheduleResponse.builder()
                .listOfScheduleTemplateResponse(List.of(scheduleTemplateResponse))
                .listOfScheduleOverrideResponse(List.of(scheduleOverrideResponse))
                .build();

        when(scheduleService.getSchedulesData(doctorId, startDate, endDate)).thenReturn(scheduleResponse);

        mockMvc.perform(get("/schedule/data")
                .param("doctorId",  doctorId.toString())
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.listOfScheduleTemplateResponse").isArray())
                .andExpect(jsonPath("$.listOfScheduleOverrideResponse").isArray());

        verify(scheduleService).getSchedulesData(doctorId, startDate, endDate);
    }
}
