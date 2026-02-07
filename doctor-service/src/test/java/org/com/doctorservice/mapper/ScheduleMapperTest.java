package org.com.doctorservice.mapper;


import org.com.doctorservice.additional.CustomDayOfTheWeek;
import org.com.doctorservice.additional.OverrideType;
import org.com.doctorservice.dto.ScheduleOverrideResponse;
import org.com.doctorservice.dto.ScheduleTemplateResponse;
import org.com.doctorservice.model.ScheduleOverride;
import org.com.doctorservice.model.ScheduleTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ScheduleMapperTest {

    private UUID id;
    private UUID doctorId;

    private ScheduleTemplate scheduleTemplate;
    private ScheduleTemplate scheduleTemplateWithNullEffectiveFromAndTo;
    private ScheduleOverride scheduleOverride;
    private ScheduleTemplateResponse scheduleTemplateResponse;
    private ScheduleOverrideResponse scheduleOverrideResponse;

    @BeforeEach
    void setUp() {
        this.id = UUID.randomUUID();
        this.doctorId = UUID.randomUUID();

        this.scheduleTemplate = ScheduleTemplate.builder()
                .id(id)
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

        this.scheduleTemplateWithNullEffectiveFromAndTo = ScheduleTemplate.builder()
                .id(id)
                .doctorId(doctorId)
                .customDayOfTheWeek(CustomDayOfTheWeek.MONDAY)
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

        this.scheduleOverride = ScheduleOverride.builder()
                .id(id)
                .doctorId(doctorId)
                .date(LocalDate.of(2026, 6, 23))
                .overrideType(OverrideType.UNAVAILABLE)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(17, 0))
                .slotDurationOfMinutes(30)
                .reason("Sick")
                .build();

        this.scheduleTemplateResponse = ScheduleTemplateResponse.builder()
                .id(id.toString())
                .doctorId(doctorId.toString())
                .dayOfTheWeek(CustomDayOfTheWeek.FRIDAY.toString())
                .startTime(LocalTime.of(9, 0).toString())
                .endTime(LocalTime.of(17, 0).toString())
                .breakStartTime(LocalTime.of(13, 0).toString())
                .breakEndTime(LocalTime.of(14, 0).toString())
                .slotDurationOfMinutes(Integer.valueOf(30).toString())
                .effectiveFrom(null)
                .effectiveTo(null)
                .build();

        this.scheduleOverrideResponse = ScheduleOverrideResponse.builder()
                .id(id.toString())
                .doctorId(doctorId.toString())
                .overrideType(OverrideType.UNAVAILABLE.toString())
                .date(LocalDate.of(2026, 2, 10).toString())
                .startTime(LocalTime.of(8, 0).toString())
                .endTime(LocalTime.of(16, 0).toString())
                .slotDurationOfMinutes(Integer.valueOf(30).toString())
                .reason("Sick")
                .build();
    }

    @Test
    void toScheduleTemplateDTOShouldSuccessfullyMapDataToDTO() {
        ScheduleTemplateResponse template = ScheduleMapper.toDTO(scheduleTemplate);

        assertEquals(id.toString(), template.getId());
        assertEquals(doctorId.toString(), template.getDoctorId());
        assertEquals(CustomDayOfTheWeek.MONDAY.toString(), template.getDayOfTheWeek());
        assertEquals(LocalTime.of(9, 0).toString(), template.getStartTime());
        assertEquals(LocalTime.of(17, 0).toString(), template.getEndTime());
    }

    @Test
    void toScheduleTemplateDTOShouldSuccessfullyMapDataWithNullEffectiveFromAndTo() {
        ScheduleTemplateResponse response = ScheduleMapper.toDTO(scheduleTemplateWithNullEffectiveFromAndTo);

        assertNull(null, response.getEffectiveFrom());
        assertNull(null, response.getEffectiveTo());
    }

    @Test
    void toScheduleOverrideDTOShouldSuccessfullyMapDataToDTO() {
        ScheduleOverrideResponse scheduleOverrideResponse = ScheduleMapper.toDTO(scheduleOverride);

        assertEquals(id.toString(), scheduleOverrideResponse.getId());
        assertEquals(doctorId.toString(), scheduleOverrideResponse.getDoctorId());
        assertEquals(OverrideType.UNAVAILABLE.toString(), scheduleOverrideResponse.getOverrideType());
        assertEquals(LocalTime.of(9, 0).toString(), scheduleOverrideResponse.getStartTime());
        assertEquals(LocalTime.of(17, 0).toString(), scheduleOverrideResponse.getEndTime());
    }
}
