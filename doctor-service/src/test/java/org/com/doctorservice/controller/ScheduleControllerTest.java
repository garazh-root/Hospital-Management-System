package org.com.doctorservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.doctorservice.additional.CustomDayOfTheWeek;
import org.com.doctorservice.exception.EmptyScheduleException;
import org.com.doctorservice.service.ScheduleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScheduleController.class)
public class ScheduleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ScheduleService scheduleService;

    @Test
    @DisplayName("GET, /schedule/{doctorId} - should return schedules by doctor id")
    void getScheduleByDoctorIdShouldReturnSchedule() throws Exception {

        UUID doctorId = UUID.randomUUID();

        ScheduleResponseDTO firstSchedule = ScheduleResponseDTO.builder()
                .scheduleStartTime(LocalTime.of(9, 0).toString())
                .scheduleEndTime(LocalTime.of(16, 0).toString())
                .scheduleDate(LocalDate.of(2025, 11, 29).toString())
                .breakStartTime(LocalTime.of(13, 0).toString())
                .breakEndTime(LocalTime.of(14, 0).toString())
                .dayOfWeek(CustomDayOfTheWeek.MONDAY.toString())
                .isDayOff(String.valueOf(false))
                .build();

        ScheduleResponseDTO secondSchedule = ScheduleResponseDTO.builder()
                .scheduleStartTime(LocalTime.of(9, 0).toString())
                .scheduleEndTime(LocalTime.of(16, 0).toString())
                .scheduleDate(LocalDate.of(2025, 11, 29).toString())
                .breakStartTime(LocalTime.of(13, 0).toString())
                .breakEndTime(LocalTime.of(14, 0).toString())
                .dayOfWeek(CustomDayOfTheWeek.WEDNESDAY.toString())
                .isDayOff(String.valueOf(false))
                .build();

        List<ScheduleResponseDTO> schedulesList = List.of(firstSchedule, secondSchedule);

        when(scheduleService.findAllByDoctorId(doctorId)).thenReturn(schedulesList);

        mockMvc.perform(get("/schedule/{doctorId}", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].scheduleStartTime").value(firstSchedule.getScheduleStartTime()))
                .andExpect(jsonPath("$[0].scheduleEndTime").value(secondSchedule.getScheduleEndTime()))
                .andExpect(jsonPath("$[0].scheduleDate").value(firstSchedule.getScheduleDate()))
                .andExpect(jsonPath("$[0].breakStartTime").value(firstSchedule.getBreakStartTime()))
                .andExpect(jsonPath("$[0].breakEndTime").value(firstSchedule.getBreakEndTime()))
                .andExpect(jsonPath("$[1].scheduleStartTime").value(secondSchedule.getScheduleStartTime()))
                .andExpect(jsonPath("$[1].scheduleEndTime").value(secondSchedule.getScheduleEndTime()))
                .andExpect(jsonPath("$[1].scheduleDate").value(secondSchedule.getScheduleDate()))
                .andExpect(jsonPath("$[1].breakStartTime").value(firstSchedule.getBreakStartTime()))
                .andExpect(jsonPath("$[1].breakEndTime").value(firstSchedule.getBreakEndTime()));

        verify(scheduleService).findAllByDoctorId(doctorId);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    @DisplayName("/GET, /schedule/{doctorId} - should get bad request if schedule is empty")
    void getScheduleByDoctorIdShouldReturnBadRequestIfScheduleIsEmpty() throws Exception {
        UUID doctorId = UUID.randomUUID();

        when(scheduleService.findAllByDoctorId(doctorId)).thenThrow(EmptyScheduleException.class);

        mockMvc.perform(get("/schedule/{doctorId}", doctorId))
                .andExpect(status().isBadRequest());

        verify(scheduleService).findAllByDoctorId(doctorId);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    @DisplayName("GET, /schedule/filterByIdAndDayOfTheWeek - should return schedules by doctor id and day of the week")
    void getSchedulesByDoctorIdAndCustomDayOfTheWeekShouldReturnListOfSchedules() throws Exception {
        UUID doctorId = UUID.randomUUID();
        CustomDayOfTheWeek day = CustomDayOfTheWeek.TUESDAY;

        ScheduleResponseDTO firstSchedule = ScheduleResponseDTO.builder()
                .scheduleStartTime(LocalTime.of(9, 0).toString())
                .scheduleEndTime(LocalTime.of(16, 0).toString())
                .scheduleDate(LocalDate.of(2025, 11, 29).toString())
                .breakStartTime(LocalTime.of(13, 0).toString())
                .breakEndTime(LocalTime.of(14, 0).toString())
                .dayOfWeek(CustomDayOfTheWeek.TUESDAY.toString())
                .isDayOff(String.valueOf(false))
                .build();

        ScheduleResponseDTO secondSchedule = ScheduleResponseDTO.builder()
                .scheduleStartTime(LocalTime.of(9, 0).toString())
                .scheduleEndTime(LocalTime.of(16, 0).toString())
                .scheduleDate(LocalDate.of(2025, 11, 29).toString())
                .breakStartTime(LocalTime.of(13, 0).toString())
                .breakEndTime(LocalTime.of(14, 0).toString())
                .dayOfWeek(CustomDayOfTheWeek.TUESDAY.toString())
                .isDayOff(String.valueOf(false))
                .build();

        List<ScheduleResponseDTO> schedulesList = List.of(firstSchedule, secondSchedule);

        when(scheduleService.findAllByDoctorIdAndCustomDayOfTheWeek(doctorId, day)).thenReturn(schedulesList);

        mockMvc.perform(get("/schedule/filterByIdAndDayOfTheWeek")
                        .param("doctorId", doctorId.toString())
                        .param("customDayOfTheWeek", day.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].scheduleStartTime").value(firstSchedule.getScheduleStartTime()))
                .andExpect(jsonPath("$[0].scheduleEndTime").value(secondSchedule.getScheduleEndTime()))
                .andExpect(jsonPath("$[0].scheduleDate").value(firstSchedule.getScheduleDate()))
                .andExpect(jsonPath("$[0].breakStartTime").value(firstSchedule.getBreakStartTime()))
                .andExpect(jsonPath("$[0].breakEndTime").value(firstSchedule.getBreakEndTime()))
                .andExpect(jsonPath("$[1].scheduleStartTime").value(secondSchedule.getScheduleStartTime()))
                .andExpect(jsonPath("$[1].scheduleEndTime").value(secondSchedule.getScheduleEndTime()))
                .andExpect(jsonPath("$[1].scheduleDate").value(secondSchedule.getScheduleDate()))
                .andExpect(jsonPath("$[1].breakStartTime").value(firstSchedule.getBreakStartTime()))
                .andExpect(jsonPath("$[1].breakEndTime").value(firstSchedule.getBreakEndTime()));

        verify(scheduleService).findAllByDoctorIdAndCustomDayOfTheWeek(doctorId, day);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    @DisplayName("/GET, /schedule/filterByDoctorIdAndDayOfTheWeek - should get bad request if schedule is empty")
    void getSchedulesByDoctorIdAndDayOfTheWeekShouldReturnBadRequestIfScheduleIsEmpty() throws Exception {
        UUID doctorId = UUID.randomUUID();
        CustomDayOfTheWeek day = CustomDayOfTheWeek.THURSDAY;

        when(scheduleService.findAllByDoctorIdAndCustomDayOfTheWeek(doctorId, day)).thenThrow(EmptyScheduleException.class);

        mockMvc.perform(get("/schedule/filterByIdAndDayOfTheWeek")
                        .param("doctorId", doctorId.toString())
                        .param("customDayOfTheWeek", day.toString()))
                .andExpect(status().isBadRequest());

        verify(scheduleService).findAllByDoctorIdAndCustomDayOfTheWeek(doctorId, day);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    @DisplayName("GET, /schedule/filterByIdAndDate - should return schedules by doctor id and start/end date")
    void getSchedulesByDoctorIdAndDateBetweenShouldReturnListOfSchedules() throws Exception {
        UUID doctorId = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2025, 11, 20);
        LocalDate endDate = LocalDate.of(2025, 11, 30);

        ScheduleResponseDTO firstSchedule = ScheduleResponseDTO.builder()
                .scheduleStartTime(LocalTime.of(9, 0).toString())
                .scheduleEndTime(LocalTime.of(16, 0).toString())
                .scheduleDate(LocalDate.of(2025, 11, 23).toString())
                .breakStartTime(LocalTime.of(13, 0).toString())
                .breakEndTime(LocalTime.of(14, 0).toString())
                .dayOfWeek(CustomDayOfTheWeek.MONDAY.toString())
                .isDayOff(String.valueOf(false))
                .build();

        ScheduleResponseDTO secondSchedule = ScheduleResponseDTO.builder()
                .scheduleStartTime(LocalTime.of(9, 0).toString())
                .scheduleEndTime(LocalTime.of(16, 0).toString())
                .scheduleDate(LocalDate.of(2025, 11, 25).toString())
                .breakStartTime(LocalTime.of(13, 0).toString())
                .breakEndTime(LocalTime.of(14, 0).toString())
                .dayOfWeek(CustomDayOfTheWeek.WEDNESDAY.toString())
                .isDayOff(String.valueOf(false))
                .build();

        ScheduleResponseDTO thirdSchedule = ScheduleResponseDTO.builder()
                .scheduleStartTime(LocalTime.of(9, 0).toString())
                .scheduleEndTime(LocalTime.of(16, 0).toString())
                .scheduleDate(LocalDate.of(2025, 11, 27).toString())
                .breakStartTime(LocalTime.of(13, 0).toString())
                .breakEndTime(LocalTime.of(14, 0).toString())
                .dayOfWeek(CustomDayOfTheWeek.FRIDAY.toString())
                .isDayOff(String.valueOf(false))
                .build();

        List<ScheduleResponseDTO> schedulesList = List.of(firstSchedule, secondSchedule, thirdSchedule);

        when(scheduleService.findAllByDoctorIdAndScheduleDateBetween(doctorId, startDate, endDate)).thenReturn(schedulesList);

        mockMvc.perform(get("/schedule/filterByIdAndDate")
                        .param("doctorId", doctorId.toString())
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].scheduleStartTime").value(firstSchedule.getScheduleStartTime()))
                .andExpect(jsonPath("$[0].scheduleEndTime").value(firstSchedule.getScheduleEndTime()))
                .andExpect(jsonPath("$[0].scheduleDate").value(firstSchedule.getScheduleDate()))
                .andExpect(jsonPath("$[1].scheduleStartTime").value(secondSchedule.getScheduleStartTime()))
                .andExpect(jsonPath("$[1].scheduleEndTime").value(secondSchedule.getScheduleEndTime()))
                .andExpect(jsonPath("$[1].scheduleDate").value(secondSchedule.getScheduleDate()))
                .andExpect(jsonPath("$[2].scheduleStartTime").value(thirdSchedule.getScheduleStartTime()))
                .andExpect(jsonPath("$[2].scheduleEndTime").value(thirdSchedule.getScheduleEndTime()))
                .andExpect(jsonPath("$[2].scheduleDate").value(thirdSchedule.getScheduleDate()));

        verify(scheduleService).findAllByDoctorIdAndScheduleDateBetween(doctorId, startDate, endDate);
        verifyNoMoreInteractions(scheduleService);

    }

    @Test
    @DisplayName("/GET, /schedule/filterByIdAndDate - should get bad request if schedule is empty")
    void getSchedulesByDoctorIdAndDateShouldReturnBadRequestIfScheduleIsEmpty() throws Exception {
        UUID doctorId = UUID.randomUUID();
        LocalDate startDate = LocalDate.of(2025, 11, 20);
        LocalDate endDate = LocalDate.of(2025, 11, 30);

        when(scheduleService.findAllByDoctorIdAndScheduleDateBetween(doctorId, startDate, endDate)).thenThrow(EmptyScheduleException.class);

        mockMvc.perform(get("/schedule/filterByIdAndDate")
                        .param("doctorId", doctorId.toString())
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("/GET, /schedule/filterByDoctorIdAndTimeBetween - should get schedules by doctor id and start/end time")
    void getSchedulesByDoctorIdAndStartTimeAndEndTimeShouldReturnListOfSchedules() throws Exception {
        UUID doctorId = UUID.randomUUID();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(16, 0);

        ScheduleResponseDTO firstSchedule = ScheduleResponseDTO.builder()
                .scheduleStartTime(LocalTime.of(9, 0).toString())
                .scheduleEndTime(LocalTime.of(16, 0).toString())
                .scheduleDate(LocalDate.of(2025, 11, 23).toString())
                .breakStartTime(LocalTime.of(13, 0).toString())
                .breakEndTime(LocalTime.of(14, 0).toString())
                .dayOfWeek(CustomDayOfTheWeek.MONDAY.toString())
                .isDayOff(String.valueOf(false))
                .build();

        ScheduleResponseDTO secondSchedule = ScheduleResponseDTO.builder()
                .scheduleStartTime(LocalTime.of(9, 0).toString())
                .scheduleEndTime(LocalTime.of(16, 0).toString())
                .scheduleDate(LocalDate.of(2025, 11, 25).toString())
                .breakStartTime(LocalTime.of(13, 0).toString())
                .breakEndTime(LocalTime.of(14, 0).toString())
                .dayOfWeek(CustomDayOfTheWeek.WEDNESDAY.toString())
                .isDayOff(String.valueOf(false))
                .build();

        List<ScheduleResponseDTO> scheduleList = List.of(firstSchedule, secondSchedule);

        when(scheduleService.findAllByDoctorIdAndStartTimeBetweenAndEndTimeBetween(doctorId, startTime, endTime)).thenReturn(scheduleList);

        mockMvc.perform(get("/schedule/filterByDoctorIdAndTimeBetween")
                        .param("doctorId", doctorId.toString())
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].scheduleStartTime").value(firstSchedule.getScheduleStartTime()))
                .andExpect(jsonPath("$[0].scheduleEndTime").value(firstSchedule.getScheduleEndTime()))
                .andExpect(jsonPath("$[0].scheduleDate").value(firstSchedule.getScheduleDate()))
                .andExpect(jsonPath("$[1].scheduleStartTime").value(secondSchedule.getScheduleStartTime()))
                .andExpect(jsonPath("$[1].scheduleEndTime").value(secondSchedule.getScheduleEndTime()))
                .andExpect(jsonPath("$[1].scheduleDate").value(secondSchedule.getScheduleDate()));

        verify(scheduleService).findAllByDoctorIdAndStartTimeBetweenAndEndTimeBetween(doctorId, startTime, endTime);
        verifyNoMoreInteractions(scheduleService);

    }

    @Test
    @DisplayName("/GET, /schedule/filterByIdAndTimeBetween - should get bad request if schedule is empty")
    void getSchedulesByDoctorIdAndTimeBetweenShouldReturnBadRequestIfScheduleIsEmpty() throws Exception {
        UUID doctorId = UUID.randomUUID();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(16, 0);

        when(scheduleService.findAllByDoctorIdAndStartTimeBetweenAndEndTimeBetween(doctorId, startTime, endTime)).thenThrow(EmptyScheduleException.class);

        mockMvc.perform(get("/schedule/filterByDoctorIdAndTimeBetween")
                        .param("doctorId", doctorId.toString())
                        .param("startTime", startTime.toString())
                        .param("endTime", endTime.toString()))
                .andExpect(status().isBadRequest());

        verify(scheduleService).findAllByDoctorIdAndStartTimeBetweenAndEndTimeBetween(doctorId, startTime, endTime);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    @DisplayName("/GET, /schedule/singleScheduleByIdAndDate - should get schedules by doctor id and date")
    void getScheduleByDoctorIdAndDateShouldReturnSchedule() throws Exception {
        UUID doctorId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2025, 11, 23);

        ScheduleResponseDTO schedule = ScheduleResponseDTO.builder()
                .scheduleStartTime(LocalTime.of(9, 0).toString())
                .scheduleEndTime(LocalTime.of(16, 0).toString())
                .scheduleDate(LocalDate.of(2025, 11, 23).toString())
                .breakStartTime(LocalTime.of(13, 0).toString())
                .breakEndTime(LocalTime.of(14, 0).toString())
                .dayOfWeek(CustomDayOfTheWeek.MONDAY.toString())
                .isDayOff(String.valueOf(false))
                .build();

        when(scheduleService.findByDoctorIdAndScheduleDate(doctorId, date)).thenReturn(schedule);

        mockMvc.perform(get("/schedule/singleScheduleFilterByDate")
                        .param("doctorId", doctorId.toString())
                        .param("scheduleDate", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scheduleStartTime").value(schedule.getScheduleStartTime()))
                .andExpect(jsonPath("$.scheduleEndTime").value(schedule.getScheduleEndTime()))
                .andExpect(jsonPath("$.scheduleDate").value(schedule.getScheduleDate()));

        verify(scheduleService).findByDoctorIdAndScheduleDate(doctorId, date);
        verifyNoMoreInteractions(scheduleService);
    }

    @Test
    @DisplayName("/GET, /schedule/singleScheduleByIdAndDate - should get bad request if schedule is empty")
    void getScheduleByDoctorIdAndDateShouldReturnBadRequestIfScheduleIsEmpty() throws Exception {
        UUID doctorId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2025, 11, 23);

        when(scheduleService.findByDoctorIdAndScheduleDate(doctorId, date)).thenThrow(EmptyScheduleException.class);

        mockMvc.perform(get("/schedule/singleScheduleFilterByDate")
                        .param("doctorId", doctorId.toString())
                        .param("scheduleDate", date.toString()))
                .andExpect(status().isBadRequest());

        verify(scheduleService).findByDoctorIdAndScheduleDate(doctorId, date);
        verifyNoMoreInteractions(scheduleService);
    }


}