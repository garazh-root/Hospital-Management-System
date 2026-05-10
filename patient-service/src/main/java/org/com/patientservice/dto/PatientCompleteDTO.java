package org.com.patientservice.dto;

import org.com.patientservice.model.genders.Gender;

import java.time.LocalDate;

public record PatientCompleteDTO (
        Gender gender,
        Double weight,
        Double height,
        LocalDate dateOfBirth,
        String address
) {
}