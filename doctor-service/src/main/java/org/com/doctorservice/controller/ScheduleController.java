package org.com.doctorservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.com.doctorservice.additional.CustomDayOfTheWeek;
import org.com.doctorservice.dto.*;
import org.com.doctorservice.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping("/template")
    public ResponseEntity<ScheduleTemplateResponse> createScheduleTemplate(
            @RequestParam UUID doctorId,
            @RequestBody ScheduleTemplateRequest scheduleTemplateRequest
    ) {
        ScheduleTemplateResponse scheduleTemplateResponse = scheduleService.createScheduleTemplate(doctorId, scheduleTemplateRequest);
        return ResponseEntity.ok().body(scheduleTemplateResponse);
    }

    @PutMapping("/templates/{templateId}")
    public ResponseEntity<ScheduleTemplateResponse> updateScheduleTemplate(
            @PathVariable UUID templateId,
            @RequestBody ScheduleTemplateRequest scheduleTemplateRequest
    ) {
        ScheduleTemplateResponse scheduleTemplateResponse = scheduleService.updateScheduleTemplate(templateId, scheduleTemplateRequest);
        return ResponseEntity.ok().body(scheduleTemplateResponse);
    }

    @DeleteMapping("/templates/{templateId}")
    public ResponseEntity<Void> deleteScheduleTemplate(@PathVariable UUID templateId) {
        scheduleService.deleteScheduleTemplate(templateId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/templates{doctorId}")
    public ResponseEntity<List<ScheduleTemplateResponse>> getScheduleTemplatesByDoctorId(@PathVariable UUID doctorId) {
        List<ScheduleTemplateResponse> scheduleTemplateResponseList = scheduleService.getScheduleTemplates(doctorId);
        return ResponseEntity.ok().body(scheduleTemplateResponseList);
    }

    @GetMapping("/templates/{doctorId}")
    public ResponseEntity<List<ScheduleTemplateResponse>> getSchedulesByDoctorIdAndDayOfTheWeek(
            @PathVariable UUID doctorId,
            @RequestParam CustomDayOfTheWeek customDayOfTheWeek
    ) {
        List<ScheduleTemplateResponse> scheduleTemplateResponseList = scheduleService.getSchedulesByDoctorIdDayOfTheWeek(
                doctorId, customDayOfTheWeek
        );
        return ResponseEntity.ok().body(scheduleTemplateResponseList);
    }

    @PostMapping("/override")
    public ResponseEntity<ScheduleOverrideResponse> createScheduleOverride(
            @RequestParam UUID doctorId, ScheduleOverrideRequest scheduleOverrideRequest
    ) {
        ScheduleOverrideResponse scheduleOverrideResponse = scheduleService.createScheduleOverride(doctorId, scheduleOverrideRequest);
        return ResponseEntity.ok().body(scheduleOverrideResponse);
    }

    @DeleteMapping("/override/{overrideId}")
    public ResponseEntity<Void> deleteScheduleOverride(@PathVariable UUID overrideId) {
        scheduleService.deleteScheduleOverride(overrideId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/overrides/{doctorId}")
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
    public ResponseEntity<List<ScheduleOverrideResponse>> getSchedulesOverridesByDoctorId (
            @PathVariable UUID doctorId
    ) {
        List<ScheduleOverrideResponse> scheduleOverrideResponseList = scheduleService.getScheduleOverridesByDoctorId(doctorId);
        return ResponseEntity.ok().body(scheduleOverrideResponseList);
    }

    @GetMapping("/data")
    public ResponseEntity<ScheduleResponse> getScheduleDataByDoctorIdAndDateBetween(
            @RequestParam UUID doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        ScheduleResponse scheduleResponse = scheduleService.getSchedulesData(doctorId, startDate, endDate);
        return ResponseEntity.ok().body(scheduleResponse);
    }
}