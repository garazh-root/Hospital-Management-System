package org.com.analyticsservice.repository;

import org.com.analyticsservice.model.DoctorMeetingStats;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DoctorMeetingRepository extends JpaRepository<DoctorMeetingStats, UUID> {
    Optional<DoctorMeetingStats> findByDoctorId(UUID doctorId);

    @Query("SELECT d FROM DoctorMeetingStats d ORDER BY d.bookedCount DESC")
    List<DoctorMeetingStats> findTopDoctorsByRanking(Pageable pageable);
}
