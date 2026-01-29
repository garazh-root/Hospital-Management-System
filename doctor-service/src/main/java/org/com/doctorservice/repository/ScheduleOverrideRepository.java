package org.com.doctorservice.repository;

import org.com.doctorservice.model.ScheduleOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleOverrideRepository extends JpaRepository<ScheduleOverride, UUID> {
    List<ScheduleOverride> findByDoctorIdAndDateBetween(UUID doctorId, LocalDate startDate, LocalDate endDate);
    List<ScheduleOverride> findByDoctorIdAndDate(UUID doctorId, LocalDate date);
    List<ScheduleOverride> findByDoctorId(UUID doctorId);
}
