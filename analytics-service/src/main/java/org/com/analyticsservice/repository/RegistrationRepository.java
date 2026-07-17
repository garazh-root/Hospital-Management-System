package org.com.analyticsservice.repository;

import org.com.analyticsservice.addtitional.Roles;
import org.com.analyticsservice.model.RegistrationStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RegistrationRepository extends JpaRepository<RegistrationStats, UUID> {
    Optional<RegistrationStats> findByRole(Roles role);
}
