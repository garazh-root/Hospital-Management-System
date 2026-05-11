package org.com.doctorservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.com.doctorservice.additional.Genders;

import java.math.BigDecimal;

public record DoctorCompleteDTO(
        @NotNull Genders gender,
        @NotBlank String specialization,
        @NotNull BigDecimal rating
) {
}
