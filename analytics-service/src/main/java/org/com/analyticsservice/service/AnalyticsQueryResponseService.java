package org.com.analyticsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.analyticsservice.dto.*;
import org.com.analyticsservice.repository.DailyMeetingRepository;
import org.com.analyticsservice.repository.DoctorMeetingRepository;
import org.com.analyticsservice.repository.RegistrationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsQueryResponseService {

    private final DailyMeetingRepository dailyMeetingRepository;
    private final DoctorMeetingRepository doctorMeetingRepository;
    private final RegistrationRepository registrationRepository;

    public List<RegistrationStatsResponse> getRegistrationStats() {
        return registrationRepository.findAll()
                .stream()
                .map(s -> new RegistrationStatsResponse(s.getRole(), s.getCount()))
                .toList();
    }

    public DailyMeetingStatsResponse getDailyStats(LocalDate date) {
        return dailyMeetingRepository.findByDate(date)
                .map(s -> new DailyMeetingStatsResponse(
                        s.getDate(),
                        s.getBookedCount(),
                        s.getCompletedCount(),
                        s.getCancelledCount()
                ))
                .orElse(new DailyMeetingStatsResponse(LocalDate.now(), 0, 0, 0));
    }

    public DailyMeetingStatsResponse getTodayStats() {
        return getDailyStats(LocalDate.now(ZoneOffset.UTC));
    }

    public DoctorMeetingStatsResponse getDoctorStats(UUID doctorId) {
        return doctorMeetingRepository.findByDoctorId(doctorId)
                .map(d -> new DoctorMeetingStatsResponse(
                        d.getDoctorId(),
                        d.getBookedCount(),
                        d.getCompletedCount(),
                        d.getCancelledCount()
                ))
                .orElse(new DoctorMeetingStatsResponse(doctorId, 0, 0, 0));
    }

    public List<DoctorMeetingStatsResponse> getTopDoctorRankings(int limit) {
        return doctorMeetingRepository.findTopDoctorsByRanking(PageRequest.of(0, limit))
                .stream()
                .map(d -> new DoctorMeetingStatsResponse(
                        d.getDoctorId(),
                        d.getBookedCount(),
                        d.getCompletedCount(),
                        d.getCancelledCount()
                ))
                .toList();
    }

    public CancellationStatsResponse getCancellationRate() {
        Double rate = dailyMeetingRepository.calculateOverallCancellationRate();
        if (rate == null) {
            rate = 0.0;
        }
        String formattedRate = String.format("%.1f%%", rate);
        return new CancellationStatsResponse(rate, formattedRate);
    }

    public OverallStatsResponse getOverallStats() {
        return new OverallStatsResponse(
                getRegistrationStats(),
                getTodayStats(),
                getCancellationRate().rate()
        );
    }
}