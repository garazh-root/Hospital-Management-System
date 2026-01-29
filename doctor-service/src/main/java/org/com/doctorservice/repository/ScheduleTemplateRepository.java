package org.com.doctorservice.repository;

import org.com.doctorservice.additional.CustomDayOfTheWeek;
import org.com.doctorservice.model.ScheduleTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ScheduleTemplateRepository extends JpaRepository<ScheduleTemplate, UUID> {
    List<ScheduleTemplate> findByDoctorIdAndActiveTrue(UUID doctorId);

    @Query("SELECT st FROM ScheduleTemplate st WHERE st.doctorId = :doctorId " +
                    "AND st.active = true " +
                    "AND (st.effectiveFrom IS NULL OR st.effectiveFrom <= :date) " +
                    "AND (st.effectiveTo IS NULL OR st.effectiveTo >= :date)")
    List<ScheduleTemplate> findActiveTemplateForDoctorOnDate(UUID doctorId, LocalDate date);

    List<ScheduleTemplate> findByDoctorIdAndCustomDayOfTheWeekAndActiveTrue(UUID doctorId, CustomDayOfTheWeek dayOfWeek);
}
