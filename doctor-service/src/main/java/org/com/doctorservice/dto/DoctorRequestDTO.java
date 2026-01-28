package org.com.doctorservice.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.doctorservice.additional.DoctorStatus;
import org.com.doctorservice.dto.validators.CreateDoctorValidationGroup;
import org.com.doctorservice.additional.Genders;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorRequestDTO {

    @NotBlank(message = "Requires first name")
    @Size(max = 50, message = "First name must be less than 50 digits")
    private String firstName;

    @NotBlank(message = "Requires last name")
    @Size(max = 50, message = "Last name must be less than 50 digits")
    private String lastName;

    @NotNull(groups = CreateDoctorValidationGroup.class, message = "Requires gender")
    private Genders gender;

    @NotBlank(message = "Requires email")
    @Email(message = "Email address must be unique")
    private String email;

    @NotBlank(message = "Requires phone number")
    private String phoneNumber;

    @NotBlank(message = "Requires specialization")
    private String specialization;

    @NotNull(groups = CreateDoctorValidationGroup.class, message = "Requires rating")
    @DecimalMin(value = "0.0", message = "Min value must be 0")
    @DecimalMax(value = "0.0", message = "Max value must be 0")
    private BigDecimal rating;

    @NotNull(message = "Requires status")
    private DoctorStatus doctorStatus;
}