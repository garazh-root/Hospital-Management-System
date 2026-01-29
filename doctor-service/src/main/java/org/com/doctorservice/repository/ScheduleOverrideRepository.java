package org.com.doctorservice.repository;

import org.com.doctorservice.model.ScheduleOverrides;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleOverrideRepository extends JpaRepository<ScheduleOverrides, UUID> {
    List<ScheduleOverrides> findByDoctorIdAndDateBetween(UUID doctorId, LocalDate startDate, LocalDate endDate);
    List<ScheduleOverrides> findByDoctorIdAndDate(UUID doctorId, LocalDate date);
    List<ScheduleOverrides> findByDoctorId(UUID doctorId);
}
