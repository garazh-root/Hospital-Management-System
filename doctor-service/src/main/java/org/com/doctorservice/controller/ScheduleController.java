package org.com.doctorservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.com.doctorservice.additional.CustomDayOfTheWeek;
import org.com.doctorservice.dto.*;
import org.com.doctorservice.service.ScheduleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/schedule")
@Tag(name = "schedule", description = "API for managing schedule templates/overrides")
public class ScheduleController {

    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/template/{doctorIdToCreateTemplate}")
    @Operation(summary = "Create schedule template")
    public ResponseEntity<ScheduleTemplateResponse> createScheduleTemplate(
            @PathVariable UUID doctorIdToCreateTemplate,
            @RequestBody ScheduleTemplateRequest scheduleTemplateRequest
    ) {
        ScheduleTemplateResponse scheduleTemplateResponse = scheduleService.createScheduleTemplate(doctorIdToCreateTemplate, scheduleTemplateRequest);
        return ResponseEntity.ok().body(scheduleTemplateResponse);
    }

    @PutMapping("/templates/{templateId}")
    @Operation(summary = "Update schedule template")
    public ResponseEntity<ScheduleTemplateResponse> updateScheduleTemplate(
            @PathVariable UUID templateId,
            @RequestBody ScheduleTemplateRequest scheduleTemplateRequest
    ) {
        ScheduleTemplateResponse scheduleTemplateResponse = scheduleService.updateScheduleTemplate(templateId, scheduleTemplateRequest);
        return ResponseEntity.ok().body(scheduleTemplateResponse);
    }

    @DeleteMapping("/templates/{templateId}")
    @Operation(summary = "Delete schedule template")
    public ResponseEntity<Void> deleteScheduleTemplate(@PathVariable UUID templateId) {
        scheduleService.deleteScheduleTemplate(templateId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/templates/{doctorId}")
    @Operation(summary = "Get schedule templates by doctor id")
    public ResponseEntity<List<ScheduleTemplateResponse>> getScheduleTemplatesByDoctorId(@PathVariable UUID doctorId) {
        List<ScheduleTemplateResponse> scheduleTemplateResponseList = scheduleService.getScheduleTemplates(doctorId);
        return ResponseEntity.ok().body(scheduleTemplateResponseList);
    }

    @GetMapping("/templates/byDay/{doctorId}")
    @Operation(summary = "Get schedule templates by doctor id and day of the week")
    public ResponseEntity<List<ScheduleTemplateResponse>> getSchedulesByDoctorIdAndDayOfTheWeek(
            @PathVariable UUID doctorId,
            @RequestParam CustomDayOfTheWeek customDayOfTheWeek
    ) {
        List<ScheduleTemplateResponse> scheduleTemplateResponseList = scheduleService.getSchedulesByDoctorIdDayOfTheWeek(
                doctorId, customDayOfTheWeek
        );
        return ResponseEntity.ok().body(scheduleTemplateResponseList);
    }

    @PostMapping("/override/{doctorIdToCreateOverride}")
    @Operation(summary = "Create schedule override")
    public ResponseEntity<ScheduleOverrideResponse> createScheduleOverride(
            @PathVariable UUID doctorIdToCreateOverride, @RequestBody ScheduleOverrideRequest scheduleOverrideRequest
    ) {
        ScheduleOverrideResponse scheduleOverrideResponse = scheduleService.createScheduleOverride(doctorIdToCreateOverride, scheduleOverrideRequest);
        return ResponseEntity.ok().body(scheduleOverrideResponse);
    }

    @DeleteMapping("/override/{overrideId}")
    @Operation(summary = "Delete schedule override")
    public ResponseEntity<Void> deleteScheduleOverride(@PathVariable UUID overrideId) {
        scheduleService.deleteScheduleOverride(overrideId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/overrides/range/{doctorId}")
    @Operation(summary = "Get schedule overrides by doctor id an date between")
    public ResponseEntity<List<ScheduleOverrideResponse>> getSchedulesOverridesByDoctorIdAndDateBetween(
            @PathVariable UUID doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<ScheduleOverrideResponse> scheduleOverrideResponseList = scheduleService.getScheduleOverridesByDoctorIdAndDateBetween(
                doctorId, startDate, endDate
        );
        return ResponseEntity.ok().body(scheduleOverrideResponseList);
    }

    @GetMapping("/overrides/{doctorId}")
    @Operation(summary = "Get schedule overrides by doctor id")
    public ResponseEntity<List<ScheduleOverrideResponse>> getSchedulesOverridesByDoctorId (
            @PathVariable UUID doctorId
    ) {
        List<ScheduleOverrideResponse> scheduleOverrideResponseList = scheduleService.getScheduleOverridesByDoctorId(doctorId);
        return ResponseEntity.ok().body(scheduleOverrideResponseList);
    }

    @GetMapping("/data")
    @Operation(summary = "Get schedule data by doctor id and date between")
    public ResponseEntity<ScheduleResponse> getScheduleDataByDoctorIdAndDateBetween(
            @RequestParam UUID doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        ScheduleResponse scheduleResponse = scheduleService.getSchedulesData(doctorId, startDate, endDate);
        return ResponseEntity.ok().body(scheduleResponse);
    }
}