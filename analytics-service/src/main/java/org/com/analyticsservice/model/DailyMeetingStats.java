package org.com.analyticsservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "daily_meeting_stats")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DailyMeetingStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "booked_count", nullable = false)
    private Integer bookedCount;

    @Column(name = "completed_count", nullable = false)
    private Integer completedCount;

    @Column(name = "cancelled_count", nullable = false)
    private Integer cancelledCount;
}