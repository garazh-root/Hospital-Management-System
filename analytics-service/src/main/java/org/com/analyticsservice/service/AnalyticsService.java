package org.com.analyticsservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.analyticsservice.addtitional.Roles;
import org.com.analyticsservice.model.DailyMeetingStats;
import org.com.analyticsservice.model.DoctorMeetingStats;
import org.com.analyticsservice.model.RegistrationStats;
import org.com.analyticsservice.repository.DailyMeetingRepository;
import org.com.analyticsservice.repository.DoctorMeetingRepository;
import org.com.analyticsservice.repository.RegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsService {

    private final DailyMeetingRepository dailyMeetingRepository;
    private final DoctorMeetingRepository doctorMeetingRepository;
    private final RegistrationRepository registrationRepository;

    public void handleRegistration(String role){
        Roles roleEnum = Roles.valueOf(role);

        RegistrationStats stats = registrationRepository.findByRole(roleEnum)
                .orElse(RegistrationStats.builder()
                        .count(0)
                        .role(roleEnum)
                        .build());

        stats.setCount(stats.getCount() + 1);
        registrationRepository.save(stats);
        log.info("Registration stats saved for role {}", role);
    }

    public void handleMeetingBooked(UUID doctorId, Instant dateTime){
        LocalDate date = dateTime.atZone(ZoneOffset.UTC).toLocalDate();
        DailyMeetingStats stats = getOrCreateDailyMeeting(date);
        stats.setBookedCount(stats.getBookedCount() + 1);
        dailyMeetingRepository.save(stats);

        DoctorMeetingStats docStats = getOrCreateDoctorMeetingStats(doctorId);
        docStats.setBookedCount(docStats.getBookedCount() + 1);
        doctorMeetingRepository.save(docStats);

        log.info("Meeting booked stats updated for doctor {} on {}",  doctorId, date);
    }

    public void handleMeetingCompleted(UUID doctorId, Instant dateTime){
        LocalDate date = dateTime.atZone(ZoneOffset.UTC).toLocalDate();
        DailyMeetingStats stats = getOrCreateDailyMeeting(date);
        stats.setCompletedCount(stats.getCompletedCount() + 1);
        dailyMeetingRepository.save(stats);

        DoctorMeetingStats docStats = getOrCreateDoctorMeetingStats(doctorId);
        docStats.setCompletedCount(docStats.getCompletedCount() + 1);
        doctorMeetingRepository.save(docStats);

        log.info("Meeting completed stats updated for doctor {} on {}",  doctorId, date);
    }

    public void handleMeetingCancelled(UUID doctorId, Instant dateTime){
        LocalDate date = dateTime.atZone(ZoneOffset.UTC).toLocalDate();
        DailyMeetingStats stats = getOrCreateDailyMeeting(date);
        stats.setCancelledCount(stats.getCancelledCount() + 1);
        dailyMeetingRepository.save(stats);

        DoctorMeetingStats docStats = getOrCreateDoctorMeetingStats(doctorId);
        docStats.setCancelledCount(docStats.getCancelledCount() + 1);
        doctorMeetingRepository.save(docStats);

        log.info("Meeting cancelled stats updated for doctor {} on {}",  doctorId, date);
    }

    @Transactional
    DoctorMeetingStats getOrCreateDoctorMeetingStats(UUID doctorId) {
        return doctorMeetingRepository.findByDoctorId(doctorId)
                .orElse(DoctorMeetingStats.builder()
                        .doctorId(doctorId)
                        .bookedCount(0)
                        .completedCount(0)
                        .cancelledCount(0)
                        .build());
    }

    @Transactional
    DailyMeetingStats getOrCreateDailyMeeting(LocalDate date) {
        return dailyMeetingRepository.findByDate(date)
                .orElse(DailyMeetingStats.builder()
                        .date(date)
                        .bookedCount(0)
                        .completedCount(0)
                        .cancelledCount(0)
                        .build());
    }
}