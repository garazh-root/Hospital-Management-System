package org.com.doctorservice.repository;

import org.com.doctorservice.model.Doctor;
import org.com.doctorservice.additional.Genders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    List<Doctor> findBySpecialization(String specialization);
    List<Doctor> findDoctorsByGender(Genders gender);
    boolean existsByEmail(String email);
    boolean existsByEmailAndDoctorIdNot(String email, UUID id);
}
