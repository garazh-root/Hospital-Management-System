package org.com.analyticsservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "doctor_meeting_stats")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DoctorMeetingStats {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "doctor_id",  nullable = false)
    private UUID doctorId;

    @Column(name = "booked_count", nullable = false)
    private Integer bookedCount;

    @Column(name = "completed_count", nullable = false)
    private Integer completedCount;

    @Column(name = "cancelled_count", nullable = false)
    private Integer cancelledCount;
}