package org.com.patientservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientResponseDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private String weight;
    private String height;
    private String email;
    private String phoneNumber;
    private String dateOfBirth;
    private String address;
    private String status;
}