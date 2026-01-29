package org.com.doctorservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.doctorservice.additional.OverrideType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "schedule_overrides")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID doctorId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, name = "override_type")
    @Enumerated(EnumType.STRING)
    private OverrideType overrideType;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "slot_duration_of_minutes")
    private Integer slotDurationOfMinutes;

    @Column(name = "reason")
    private String reason;

    private LocalDate createdAt;
}