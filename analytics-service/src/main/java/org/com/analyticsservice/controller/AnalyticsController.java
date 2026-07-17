package org.com.analyticsservice.controller;

import lombok.RequiredArgsConstructor;
import org.com.analyticsservice.dto.*;
import org.com.analyticsservice.service.AnalyticsQueryResponseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsQueryResponseService analyticsQueryResponseService;

    @GetMapping("/registrations")
    public ResponseEntity<List<RegistrationStatsResponse>> getRegistrationStats() {
        List<RegistrationStatsResponse> registrationStats = analyticsQueryResponseService.getRegistrationStats();
        return ResponseEntity.ok().body(registrationStats);
    }

    @GetMapping("/meetings/daily")
    public ResponseEntity<DailyMeetingStatsResponse> getDailyMeetingStats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyMeetingStatsResponse dailyMeetingStatsResponse = analyticsQueryResponseService.getDailyStats(date);
        return ResponseEntity.ok().body(dailyMeetingStatsResponse);
    }

    @GetMapping("/meetings/today")
    public ResponseEntity<DailyMeetingStatsResponse> getTodayStats() {
        DailyMeetingStatsResponse dailyMeetingStatsResponse = analyticsQueryResponseService.getTodayStats();
        return ResponseEntity.ok().body(dailyMeetingStatsResponse);
    }

    @GetMapping("/meeting/doctor/{doctorId}")
    public ResponseEntity<DoctorMeetingStatsResponse> getDoctorStats(
            @PathVariable UUID doctorId) {
        DoctorMeetingStatsResponse doctorMeetingStatsResponse = analyticsQueryResponseService.getDoctorStats(doctorId);
        return ResponseEntity.ok().body(doctorMeetingStatsResponse);
    }

    @GetMapping("/meeting/top-doctors")
    public ResponseEntity<List<DoctorMeetingStatsResponse>> getTopDoctorsStats(
            @RequestParam(defaultValue = "5") int limit
    ) {
        List<DoctorMeetingStatsResponse> doctorMeetingStatsResponses = analyticsQueryResponseService.getTopDoctorRankings(limit);
        return ResponseEntity.ok().body(doctorMeetingStatsResponses);
    }

    @GetMapping("/cancellation-rate")
    public ResponseEntity<CancellationStatsResponse> getCancellationStats() {
        CancellationStatsResponse cancellationStatsResponse = analyticsQueryResponseService.getCancellationRate();
        return ResponseEntity.ok().body(cancellationStatsResponse);
    }

    @GetMapping("/overview")
    public ResponseEntity<OverallStatsResponse>  getOverallStats() {
        OverallStatsResponse overallStatsResponse = analyticsQueryResponseService.getOverallStats();
        return ResponseEntity.ok().body(overallStatsResponse);
    }
}