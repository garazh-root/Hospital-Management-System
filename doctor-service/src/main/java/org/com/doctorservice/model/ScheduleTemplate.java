package org.com.doctorservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.doctorservice.additional.CustomDayOfTheWeek;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "schedule_templates")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private UUID doctorId;

    @Column(nullable = false, name = "day_of_week")
    @Enumerated(EnumType.STRING)
    private CustomDayOfTheWeek customDayOfTheWeek;

    @Column(nullable = false, name = "start_time")
    private LocalTime startTime;

    @Column(nullable = false, name = "end_time")
    private LocalTime endTime;

    @Column(nullable = false, name = "break_start_time")
    private LocalTime breakStartTime;

    @Column(nullable = false, name = "break_end_time")
    private LocalTime breakEndTime;

    @Column(nullable = false, name = "slot_duration_of_minutes")
    private Integer slotDurationOfMinutes;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;
}