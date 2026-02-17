package org.com.doctorservice.service;

import org.com.doctorservice.additional.CustomDayOfTheWeek;
import org.com.doctorservice.additional.OverrideType;
import org.com.doctorservice.dto.*;
import org.com.doctorservice.exception.DoctorNotFoundException;
import org.com.doctorservice.exception.OverrideAlreadyExistsException;
import org.com.doctorservice.exception.ScheduleNotFoundException;
import org.com.doctorservice.model.ScheduleOverride;
import org.com.doctorservice.model.ScheduleTemplate;
import org.com.doctorservice.repository.DoctorRepository;
import org.com.doctorservice.repository.ScheduleOverrideRepository;
import org.com.doctorservice.repository.ScheduleTemplateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
public class ScheduleServiceTest {

    @Mock
    private ScheduleTemplateRepository scheduleTemplateRepository;

    @Mock
    private ScheduleOverrideRepository scheduleOverrideRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private ScheduleService scheduleService;

    private UUID scheduleTemplateId;
    private UUID scheduleOverrideId;
    private UUID doctorId;

    private ScheduleTemplateRequest scheduleTemplateRequest;
    private ScheduleOverrideRequest scheduleOverrideRequest;
    private ScheduleTemplate scheduleTemplate;
    private ScheduleOverride scheduleOverride;
    private ScheduleTemplateResponse scheduleTemplateResponse;
    private ScheduleOverrideResponse scheduleOverrideResponse;

    @BeforeEach
    void setUp() {
        scheduleTemplateId = UUID.randomUUID();
        scheduleOverrideId = UUID.randomUUID();
        doctorId = UUID.randomUUID();

        this.scheduleTemplateRequest = ScheduleTemplateRequest.builder()
                .dayOfTheWeek(CustomDayOfTheWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .breakStartTime(LocalTime.of(13, 0))
                .breakEndTime(LocalTime.of(14, 0))
                .slotDuration(30)
                .build();

        this.scheduleOverrideRequest = ScheduleOverrideRequest.builder()
                .date(LocalDate.of(2026, 6, 23))
                .overrideType(OverrideType.UNAVAILABLE)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .slotDurationOfMinutes(30)
                .reason("Sick")
                .build();

        this.scheduleTemplateResponse = ScheduleTemplateResponse.builder()
                .id(scheduleTemplateId.toString())
                .doctorId(doctorId.toString())
                .dayOfTheWeek(CustomDayOfTheWeek.MONDAY.toString())
                .startTime(LocalTime.of(9, 0).toString())
                .endTime(LocalTime.of(17, 0).toString())
                .breakEndTime(LocalTime.of(13, 0).toString())
                .breakEndTime(LocalTime.of(14, 0).toString())
                .slotDurationOfMinutes(Integer.valueOf(30).toString())
                .build();

        this.scheduleOverrideResponse = ScheduleOverrideResponse.builder()
                .id(scheduleOverrideId.toString())
                .doctorId(doctorId.toString())
                .date(LocalDate.of(2026, 6, 23).toString())
                .overrideType(OverrideType.UNAVAILABLE.toString())
                .startTime(LocalTime.of(9, 0).toString())
                .endTime(LocalTime.of(17, 0).toString())
                .slotDurationOfMinutes(Integer.valueOf(30).toString())
                .build();

        this.scheduleTemplate = ScheduleTemplate.builder()
                .id(scheduleTemplateId)
                .doctorId(doctorId)
                .customDayOfTheWeek(CustomDayOfTheWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .breakStartTime(LocalTime.of(13, 0))
                .breakEndTime(LocalTime.of(14, 0))
                .slotDurationOfMinutes(30)
                .active(true)
                .createdAt(LocalDate.now())
                .build();

        this.scheduleOverride = ScheduleOverride.builder()
                .id(scheduleOverrideId)
                .doctorId(doctorId)
                .date(LocalDate.of(2026, 6, 23))
                .overrideType(OverrideType.UNAVAILABLE)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .slotDurationOfMinutes(30)
                .reason("Sick")
                .build();
    }

    @Test
    void createScheduleTemplateShouldSuccessfullyCreateScheduleTemplate() {
        when(doctorRepository.existsById(doctorId)).thenReturn(true);
        when(scheduleTemplateRepository.save(any(ScheduleTemplate.class))).thenReturn(scheduleTemplate);

        ScheduleTemplateResponse scheduleTemplate = scheduleService.createScheduleTemplate(doctorId, scheduleTemplateRequest);

        Assertions.assertNotNull(scheduleTemplate);

        assertEquals(scheduleTemplate.getStartTime(), LocalTime.of(9, 0).toString());
        assertEquals(scheduleTemplate.getEndTime(), LocalTime.of(17, 0).toString());
        assertEquals(scheduleTemplate.getBreakStartTime(), LocalTime.of(13, 0).toString());
        assertEquals(scheduleTemplate.getBreakEndTime(), LocalTime.of(14, 0).toString());
        assertEquals(scheduleTemplate.getSlotDurationOfMinutes(), Integer.valueOf(30).toString());

        verify(doctorRepository).existsById(doctorId);
        verify(scheduleTemplateRepository).save(any(ScheduleTemplate.class));
    }

    @Test
    void createScheduleTemplateShouldThrowExceptionWhenDoctorDoesNotExist() {
        when(doctorRepository.existsById(doctorId)).thenReturn(false);

        Assertions.assertThrows(DoctorNotFoundException.class, () -> scheduleService.createScheduleTemplate(doctorId, scheduleTemplateRequest));

        verify(doctorRepository, times(1)).existsById(doctorId);
        verify(scheduleTemplateRepository, never()).save(any(ScheduleTemplate.class));
    }

    @Test
    void updateScheduleTemplateShouldSuccessfullyUpdateScheduleTemplate() {
        when(scheduleTemplateRepository.findById(scheduleTemplateId)).thenReturn(Optional.of(scheduleTemplate));
        when(scheduleTemplateRepository.save(any(ScheduleTemplate.class))).thenReturn(scheduleTemplate);

        ScheduleTemplateResponse scheduleTemplateResponse = scheduleService.updateScheduleTemplate(scheduleTemplateId, scheduleTemplateRequest);

        Assertions.assertNotNull(scheduleTemplateResponse);

        assertEquals(scheduleTemplateResponse.getStartTime(), LocalTime.of(9, 0).toString());
        assertEquals(scheduleTemplateResponse.getEndTime(), LocalTime.of(17, 0).toString());
        assertEquals(scheduleTemplateResponse.getBreakStartTime(), LocalTime.of(13, 0).toString());
        assertEquals(scheduleTemplateResponse.getBreakEndTime(), LocalTime.of(14, 0).toString());
        assertEquals(scheduleTemplateResponse.getSlotDurationOfMinutes(), Integer.valueOf(30).toString());

        verify(scheduleTemplateRepository).findById(scheduleTemplateId);
        verify(scheduleTemplateRepository).save(any(ScheduleTemplate.class));
    }

    @Test
    void updateScheduleTemplateShouldThrowExceptionWhenScheduleNotFound() {
        when(scheduleTemplateRepository.findById(scheduleTemplateId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ScheduleNotFoundException.class, () -> scheduleService.updateScheduleTemplate(scheduleTemplateId, scheduleTemplateRequest));

        verify(scheduleTemplateRepository, times(1)).findById(scheduleTemplateId);
        verify(scheduleTemplateRepository, never()).save(any(ScheduleTemplate.class));
    }

    @Test
    void deleteScheduleTemplateShouldSuccessfullyDeleteScheduleTemplate() {
        when(scheduleTemplateRepository.findById(scheduleTemplateId)).thenReturn(Optional.of(scheduleTemplate));
        when(scheduleTemplateRepository.save(any(ScheduleTemplate.class))).thenReturn(scheduleTemplate);

        scheduleService.deleteScheduleTemplate(scheduleTemplateId);

        Assertions.assertFalse(scheduleTemplate.getActive());

        verify(scheduleTemplateRepository, times(1)).findById(scheduleTemplateId);
        verify(scheduleTemplateRepository, times(1)).save(any(ScheduleTemplate.class));
    }

    @Test
    void deleteScheduleTemplateShouldThrowExceptionWhenScheduleNotFound() {
        when(scheduleTemplateRepository.findById(scheduleTemplateId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ScheduleNotFoundException.class, () -> scheduleService.deleteScheduleTemplate(scheduleTemplateId));

        verify(scheduleTemplateRepository, times(1)).findById(scheduleTemplateId);
    }

    @Test
    void getSchedulesShouldSuccessfullyGetSchedules() {
        List<ScheduleTemplate> templates = List.of(scheduleTemplate);

        when(scheduleTemplateRepository.findByDoctorIdAndActiveTrue(scheduleTemplateId)).thenReturn(templates);

        List<ScheduleTemplateResponse> templateResponses = scheduleService.getScheduleTemplates(scheduleTemplateId);

        Assertions.assertNotNull(templateResponses);

        verify(scheduleTemplateRepository, times(1)).findByDoctorIdAndActiveTrue(scheduleTemplateId);
    }

    @Test
    void getSchedulesShouldReturnEmptyListWhenScheduleNotFound() {
        when(scheduleTemplateRepository.findByDoctorIdAndActiveTrue(scheduleTemplateId)).thenReturn(Collections.emptyList());

        List<ScheduleTemplateResponse> templateResponses = scheduleService.getScheduleTemplates(scheduleTemplateId);

        Assertions.assertTrue(templateResponses.isEmpty());

        verify(scheduleTemplateRepository, times(1)).findByDoctorIdAndActiveTrue(scheduleTemplateId);
    }

    @Test
    void getScheduleByDayOfWeekShouldReturnListOfSchedulesByDayOfWeek() {
        List<ScheduleTemplate> templates = List.of(scheduleTemplate);

        when(scheduleTemplateRepository.findByDoctorIdAndCustomDayOfTheWeekAndActiveTrue(doctorId, CustomDayOfTheWeek.MONDAY)).thenReturn(templates);

        List<ScheduleTemplateResponse> scheduleTemplateResponseList = scheduleService.getSchedulesByDoctorIdDayOfTheWeek(doctorId, CustomDayOfTheWeek.MONDAY);

        Assertions.assertNotNull(scheduleTemplateResponseList);

        verify(scheduleTemplateRepository, times(1)).findByDoctorIdAndCustomDayOfTheWeekAndActiveTrue(doctorId, CustomDayOfTheWeek.MONDAY);
    }

    @Test
    void getScheduleByDayOfWeekShouldReturnEmptyListWhenScheduleNotFound() {
        when(scheduleTemplateRepository.findByDoctorIdAndCustomDayOfTheWeekAndActiveTrue(doctorId, CustomDayOfTheWeek.MONDAY)).thenReturn(Collections.emptyList());

        List<ScheduleTemplateResponse> scheduleTemplateResponseList = scheduleService.getSchedulesByDoctorIdDayOfTheWeek(doctorId, CustomDayOfTheWeek.MONDAY);

        Assertions.assertTrue(scheduleTemplateResponseList.isEmpty());

        verify(scheduleTemplateRepository, times(1)).findByDoctorIdAndCustomDayOfTheWeekAndActiveTrue(doctorId, CustomDayOfTheWeek.MONDAY);
    }

    @Test
    void createScheduleOverrideShouldSuccessFullyCreateScheduleOverride() {
        when(doctorRepository.existsById(doctorId)).thenReturn(true);
        when(scheduleOverrideRepository.findByDoctorIdAndDate(doctorId, scheduleOverride.getDate())).thenReturn(Collections.emptyList());

        when(scheduleOverrideRepository.save(any(ScheduleOverride.class))).thenReturn(scheduleOverride);

        ScheduleOverrideResponse scheduleOverride = scheduleService.createScheduleOverride(doctorId, scheduleOverrideRequest);

        Assertions.assertNotNull(scheduleOverride);

        assertEquals(LocalDate.of(2026, 6, 23).toString(),  scheduleOverride.getDate());
        assertEquals(LocalTime.of(9, 0).toString(), scheduleOverride.getStartTime());
        assertEquals(LocalTime.of(17, 0).toString(), scheduleOverride.getEndTime());
        assertEquals("UNAVAILABLE", scheduleOverride.getOverrideType());

        verify(doctorRepository, times(1)).existsById(doctorId);
        verify(scheduleOverrideRepository, times(1)).findByDoctorIdAndDate(doctorId, scheduleOverrideRequest.getDate());
        verify(scheduleOverrideRepository, times(1)).save(any(ScheduleOverride.class));
    }

    @Test
    void createScheduleOverrideShouldThrowExceptionWhenDoctorDoesNotExist() {
        when(doctorRepository.existsById(doctorId)).thenReturn(false);

        Assertions.assertThrows(DoctorNotFoundException.class, () -> scheduleService.createScheduleOverride(doctorId, scheduleOverrideRequest));

        verify(doctorRepository, times(1)).existsById(doctorId);
        verify(scheduleOverrideRepository, never()).save(any(ScheduleOverride.class));
    }

    @Test
    void createScheduleOverrideShouldThrowExceptionWhenOverrideAlreadyExists() {
        when(doctorRepository.existsById(doctorId)).thenReturn(true);
        when(scheduleOverrideRepository.findByDoctorIdAndDate(doctorId, scheduleOverride.getDate())).thenReturn(List.of(scheduleOverride));

        Assertions.assertThrows(OverrideAlreadyExistsException.class, () -> scheduleService.createScheduleOverride(doctorId, scheduleOverrideRequest));

        verify(doctorRepository, times(1)).existsById(doctorId);
        verify(scheduleOverrideRepository, times(1)).findByDoctorIdAndDate(doctorId, scheduleOverride.getDate());
        verify(scheduleOverrideRepository, never()).save(any(ScheduleOverride.class));
    }

    @Test
    void deleteScheduleOverrideShouldSuccessfullyDeleteScheduleOverride() {
        when(scheduleOverrideRepository.findById(scheduleOverride.getId())).thenReturn(Optional.of(scheduleOverride));

        doNothing().when(scheduleOverrideRepository).delete(scheduleOverride);

        scheduleService.deleteScheduleOverride(scheduleOverrideId);

        verify(scheduleOverrideRepository, times(1)).findById(scheduleOverride.getId());
        verify(scheduleOverrideRepository, times(1)).delete(scheduleOverride);
    }

    @Test
    void deleteScheduleOverrideShouldThrowExceptionWhenOverrideNotFound() {
        when(scheduleOverrideRepository.findById(scheduleOverride.getId())).thenReturn(Optional.empty());

        Assertions.assertThrows(ScheduleNotFoundException.class, () -> scheduleService.deleteScheduleOverride(scheduleOverrideId));

        verify(scheduleOverrideRepository, times(1)).findById(scheduleOverride.getId());
    }

    @Test
    void getSchedulesOverridesByDoctorIdAndDateBetweenShouldReturnListOfSchedulesOverrides () {
        LocalDate startDate = LocalDate.of(2026, 6, 23);
        LocalDate endDate = LocalDate.of(2026, 6, 24);

        List<ScheduleOverride> scheduleOverrides = List.of(scheduleOverride);

        when(scheduleOverrideRepository.findByDoctorIdAndDateBetween(doctorId, startDate, endDate)).thenReturn(scheduleOverrides);

        List<ScheduleOverrideResponse> list = scheduleService.getScheduleOverridesByDoctorIdAndDateBetween(
                doctorId, startDate, endDate);

        Assertions.assertNotNull(list);
        assertEquals(1, list.size());

        verify(scheduleOverrideRepository, times(1)).findByDoctorIdAndDateBetween(doctorId, startDate, endDate);
    }

    @Test
    void getSchedulesOverridesByDoctorIdAndDateBetweenShouldReturnEmptyList() {
        LocalDate startDate = LocalDate.of(2026, 6, 23);
        LocalDate endDate = LocalDate.of(2026, 6, 24);

        when(scheduleOverrideRepository.findByDoctorIdAndDateBetween(doctorId, startDate, endDate)).thenReturn(Collections.emptyList());

        List<ScheduleOverrideResponse> list = scheduleService.getScheduleOverridesByDoctorIdAndDateBetween(doctorId, startDate, endDate);

        Assertions.assertTrue(list.isEmpty());
        verify(scheduleOverrideRepository, times(1)).findByDoctorIdAndDateBetween(doctorId, startDate, endDate);
    }

    @Test
    void getSchedulesOverridesByDoctorIdShouldReturnListOfSchedulesOverrides () {
        List<ScheduleOverride> scheduleOverrides = List.of(scheduleOverride);

        when(scheduleOverrideRepository.findByDoctorId(doctorId)).thenReturn(scheduleOverrides);

        List<ScheduleOverrideResponse> list = scheduleService.getScheduleOverridesByDoctorId(doctorId);

        Assertions.assertNotNull(list);
        assertEquals(1, list.size());

        verify(scheduleOverrideRepository, times(1)).findByDoctorId(doctorId);
    }

    @Test
    void getSchedulesOverridesShouldReturnEmptyListWhenScheduleNotFound() {
        when(scheduleOverrideRepository.findByDoctorId(doctorId)).thenReturn(Collections.emptyList());

        List<ScheduleOverrideResponse> list = scheduleService.getScheduleOverridesByDoctorId(doctorId);

        Assertions.assertTrue(list.isEmpty());
        verify(scheduleOverrideRepository, times(1)).findByDoctorId(doctorId);
    }

    @Test
    void getSchedulesDataShouldReturnListOfSchedulesData() {
        LocalDate startDate = LocalDate.of(2026, 6, 1);
        LocalDate endDate = LocalDate.of(2026, 6, 30);
        List<ScheduleTemplate> listOfScheduleTemplate = List.of(scheduleTemplate);
        List<ScheduleOverride> listOfScheduleOverride = List.of(scheduleOverride);

        when(scheduleTemplateRepository.findByDoctorIdAndActiveTrue(doctorId)).thenReturn(listOfScheduleTemplate);
        when(scheduleOverrideRepository.findByDoctorIdAndDateBetween(doctorId, startDate, endDate)).thenReturn(listOfScheduleOverride);

        ScheduleResponse scheduleResponse = scheduleService.getSchedulesData(doctorId, startDate, endDate);

        Assertions.assertNotNull(scheduleResponse);
        Assertions.assertNotNull(scheduleResponse.getListOfScheduleTemplateResponse());
        Assertions.assertNotNull(scheduleResponse.getListOfScheduleOverrideResponse());
        verify(scheduleTemplateRepository, times(1)).findByDoctorIdAndActiveTrue(doctorId);
        verify(scheduleOverrideRepository, times(1)).findByDoctorIdAndDateBetween(doctorId, startDate, endDate);
    }

    @Test
    void getSchedulesDataShouldReturnEmptyListWhenScheduleNotFound() {
        LocalDate startDate = LocalDate.of(2026, 6, 1);
        LocalDate endDate = LocalDate.of(2026, 6, 30);

        when(scheduleTemplateRepository.findByDoctorIdAndActiveTrue(doctorId)).thenReturn(Collections.emptyList());
        when(scheduleOverrideRepository.findByDoctorIdAndDateBetween(doctorId, startDate, endDate)).thenReturn(Collections.emptyList());

        ScheduleResponse scheduleResponse = scheduleService.getSchedulesData(doctorId, startDate, endDate);

        Assertions.assertNotNull(scheduleResponse);
        Assertions.assertTrue(scheduleResponse.getListOfScheduleTemplateResponse().isEmpty());
        Assertions.assertTrue(scheduleResponse.getListOfScheduleOverrideResponse().isEmpty());

        verify(scheduleTemplateRepository, times(1)).findByDoctorIdAndActiveTrue(doctorId);
        verify(scheduleOverrideRepository, times(1)).findByDoctorIdAndDateBetween(doctorId, startDate, endDate);
    }

    @Test
    void getSchedulesShouldReturnDatesProperlyIfDataExist () {
        LocalDate startDate = LocalDate.of(2026, 6, 1);
        LocalDate endDate = LocalDate.of(2026, 6, 30);
        List<ScheduleTemplate> templateList = List.of(scheduleTemplate);
        List<ScheduleOverride> overrideList = List.of(scheduleOverride);

        when(scheduleTemplateRepository.findByDoctorIdAndActiveTrue(doctorId)).thenReturn(templateList);
        when(scheduleOverrideRepository.findByDoctorIdAndDateBetween(doctorId, startDate, endDate)).thenReturn(overrideList);

        ScheduleResponse scheduleResponse = scheduleService.getSchedulesData(doctorId, startDate, endDate);

        Assertions.assertNotNull(scheduleResponse);
        Assertions.assertNotNull(scheduleResponse.getListOfScheduleTemplateResponse());
        Assertions.assertNotNull(scheduleResponse.getListOfScheduleOverrideResponse());

        verify(scheduleTemplateRepository, times(1)).findByDoctorIdAndActiveTrue(doctorId);
        verify(scheduleOverrideRepository, times(1)).findByDoctorIdAndDateBetween(doctorId, startDate, endDate);
    }

    @Test
    void getSchedulesDataShouldReturnEmptyListIfNoData () {
        LocalDate startDate = LocalDate.of(2026, 6, 1);
        LocalDate endDate = LocalDate.of(2026, 6, 30);

        when(scheduleTemplateRepository.findByDoctorIdAndActiveTrue(doctorId)).thenReturn(Collections.emptyList());
        when(scheduleOverrideRepository.findByDoctorIdAndDateBetween(doctorId, startDate, endDate)).thenReturn(Collections.emptyList());

        ScheduleResponse scheduleResponse = scheduleService.getSchedulesData(doctorId, startDate, endDate);

        Assertions.assertTrue(scheduleResponse.getListOfScheduleOverrideResponse().isEmpty());
        Assertions.assertTrue(scheduleResponse.getListOfScheduleTemplateResponse().isEmpty());

        verify(scheduleTemplateRepository, times(1)).findByDoctorIdAndActiveTrue(doctorId);
        verify(scheduleOverrideRepository, times(1)).findByDoctorIdAndDateBetween(doctorId, startDate, endDate);
    }

    @Test
    void getSchedulesShouldFilterOnEffectiveDatesProperly () {
        LocalDate startDate = LocalDate.of(2025, 7, 1);
        LocalDate endDate = LocalDate.of(2025, 7, 30);

        ScheduleTemplate invalidTemplate = ScheduleTemplate.builder()
                .id(scheduleTemplateId)
                .doctorId(doctorId)
                .customDayOfTheWeek(CustomDayOfTheWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .breakStartTime(LocalTime.of(13, 0))
                .breakEndTime(LocalTime.of(14, 0))
                .slotDurationOfMinutes(30)
                .effectiveFrom(LocalDate.of(2025, 1, 1))
                .effectiveTo(LocalDate.of(2025, 6, 30))
                .active(true)
                .createdAt(LocalDate.now())
                .build();

        ScheduleTemplate validTemplate = ScheduleTemplate.builder()
                .id(scheduleTemplateId)
                .doctorId(doctorId)
                .customDayOfTheWeek(CustomDayOfTheWeek.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .breakStartTime(LocalTime.of(13, 0))
                .breakEndTime(LocalTime.of(14, 0))
                .slotDurationOfMinutes(30)
                .effectiveFrom(LocalDate.of(2025, 7, 1))
                .effectiveTo(LocalDate.of(2025, 7, 30))
                .active(true)
                .createdAt(LocalDate.now())
                .build();

        List<ScheduleTemplate> templateList = List.of(invalidTemplate, validTemplate);

        when(scheduleTemplateRepository.findByDoctorIdAndActiveTrue(doctorId)).thenReturn(templateList);
        when(scheduleOverrideRepository.findByDoctorIdAndDateBetween(doctorId, startDate, endDate)).thenReturn(Collections.emptyList());

        ScheduleResponse scheduleResponse = scheduleService.getSchedulesData(doctorId, startDate, endDate);

        Assertions.assertNotNull(scheduleResponse);

        assertEquals(1, scheduleResponse.getListOfScheduleTemplateResponse().size());

        verify(scheduleTemplateRepository, times(1)).findByDoctorIdAndActiveTrue(doctorId);
        verify(scheduleOverrideRepository, times(1)).findByDoctorIdAndDateBetween(doctorId, startDate, endDate);
    }

    @Test
    void getSchedulesDataShouldFilterOnEffectiveDatesIfEffectiveFromAndToAreNull () {
        LocalDate startDate = LocalDate.of(2025, 7, 1);
        LocalDate endDate = LocalDate.of(2025, 7, 30);

        ScheduleTemplate invalidTemplate = ScheduleTemplate.builder()
                .id(scheduleTemplateId)
                .doctorId(doctorId)
                .customDayOfTheWeek(CustomDayOfTheWeek.WEDNESDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .breakStartTime(LocalTime.of(13, 0))
                .breakEndTime(LocalTime.of(14, 0))
                .slotDurationOfMinutes(30)
                .effectiveFrom(null)
                .effectiveTo(null)
                .active(true)
                .createdAt(LocalDate.now())
                .build();

        when(scheduleTemplateRepository.findByDoctorIdAndActiveTrue(doctorId)).thenReturn(List.of(invalidTemplate));
        when(scheduleOverrideRepository.findByDoctorIdAndDateBetween(doctorId, startDate, endDate)).thenReturn(Collections.emptyList());

        ScheduleResponse scheduleResponse = scheduleService.getSchedulesData(doctorId, startDate, endDate);

        Assertions.assertNotNull(scheduleResponse);
        assertEquals(1, scheduleResponse.getListOfScheduleTemplateResponse().size());

        verify(scheduleTemplateRepository, times(1)).findByDoctorIdAndActiveTrue(doctorId);
        verify(scheduleOverrideRepository, times(1)).findByDoctorIdAndDateBetween(doctorId, startDate, endDate);
    }

    @Test
    void getSchedulesDataShouldNotFilterTemplatesThatEndBeforeStartDate () {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        LocalDate endDate = LocalDate.of(2025, 5, 30);

        ScheduleTemplate invalidTemplate = ScheduleTemplate.builder()
                .id(scheduleTemplateId)
                .doctorId(doctorId)
                .customDayOfTheWeek(CustomDayOfTheWeek.WEDNESDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .breakStartTime(LocalTime.of(13, 0))
                .breakEndTime(LocalTime.of(14, 0))
                .slotDurationOfMinutes(30)
                .effectiveFrom(LocalDate.of(2025, 4, 1))
                .effectiveTo(LocalDate.of(2025, 4, 30))
                .active(true)
                .createdAt(LocalDate.now())
                .build();

        when(scheduleTemplateRepository.findByDoctorIdAndActiveTrue(doctorId)).thenReturn(List.of(invalidTemplate));
        when(scheduleOverrideRepository.findByDoctorIdAndDateBetween(doctorId, startDate, endDate)).thenReturn(Collections.emptyList());

        ScheduleResponse scheduleResponse =  scheduleService.getSchedulesData(doctorId, startDate, endDate);

        Assertions.assertTrue(scheduleResponse.getListOfScheduleTemplateResponse().isEmpty());

        verify(scheduleTemplateRepository, times(1)).findByDoctorIdAndActiveTrue(doctorId);
        verify(scheduleOverrideRepository, times(1)).findByDoctorIdAndDateBetween(doctorId, startDate, endDate);
    }

    @Test
    void getSchedulesDataShouldNotFilterTemplatesThatStartsAfterEndDate () {
        LocalDate startDate = LocalDate.of(2025, 5, 1);
        LocalDate endDate = LocalDate.of(2025, 5, 30);

        ScheduleTemplate invalidTemplate = ScheduleTemplate.builder()
                .id(scheduleTemplateId)
                .doctorId(doctorId)
                .customDayOfTheWeek(CustomDayOfTheWeek.WEDNESDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .breakStartTime(LocalTime.of(13, 0))
                .breakEndTime(LocalTime.of(14, 0))
                .slotDurationOfMinutes(30)
                .effectiveFrom(LocalDate.of(2025, 6, 1))
                .effectiveTo(LocalDate.of(2025, 6, 30))
                .active(true)
                .createdAt(LocalDate.now())
                .build();

        when(scheduleTemplateRepository.findByDoctorIdAndActiveTrue(doctorId)).thenReturn(List.of(invalidTemplate));
        when(scheduleOverrideRepository.findByDoctorIdAndDateBetween(doctorId, startDate, endDate)).thenReturn(Collections.emptyList());

        ScheduleResponse scheduleResponse =  scheduleService.getSchedulesData(doctorId, startDate, endDate);

        Assertions.assertTrue(scheduleResponse.getListOfScheduleTemplateResponse().isEmpty());

        verify(scheduleTemplateRepository, times(1)).findByDoctorIdAndActiveTrue(doctorId);
        verify(scheduleOverrideRepository, times(1)).findByDoctorIdAndDateBetween(doctorId, startDate, endDate);
    }
}