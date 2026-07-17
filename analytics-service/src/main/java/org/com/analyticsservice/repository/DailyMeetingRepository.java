package org.com.analyticsservice.repository;

import org.com.analyticsservice.model.DailyMeetingStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DailyMeetingRepository extends JpaRepository<DailyMeetingStats, UUID> {
    Optional<DailyMeetingStats> findByDate(LocalDate date);

    @Query(value = """
    SELECT SUM(cancelled_count) * 100.0 /
           NULLIF(SUM(booked_count), 0)
    FROM daily_meeting_stats
    """, nativeQuery = true)
    Double calculateOverallCancellationRate();
}
