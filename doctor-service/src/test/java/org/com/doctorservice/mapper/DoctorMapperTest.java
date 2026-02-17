package org.com.doctorservice.mapper;

import org.com.doctorservice.additional.DoctorStatus;
import org.com.doctorservice.dto.DoctorRequestDTO;
import org.com.doctorservice.dto.DoctorResponseDTO;
import org.com.doctorservice.model.Doctor;
import org.com.doctorservice.additional.Genders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DoctorMapperTest {

    private Doctor doctor;
    private DoctorResponseDTO doctorResponseDTO;
    private DoctorRequestDTO doctorRequestDTO;

    private UUID doctorId;

    @BeforeEach
    void setUp() {
        this.doctorId = UUID.randomUUID();

        this.doctor = Doctor.builder()
                .doctorId(doctorId)
                .firstName("Mark")
                .lastName("Grayson")
                .gender(Genders.MALE)
                .email("graysonm@test.com")
                .phoneNumber("+13324453")
                .specialization("Cardiologist")
                .rating(BigDecimal.valueOf(0.0))
                .doctorStatus(DoctorStatus.ACTIVE)
                .build();

        this.doctorRequestDTO = DoctorRequestDTO.builder()
                .firstName("Mark")
                .lastName("Grayson")
                .gender(Genders.MALE)
                .phoneNumber("13324453")
                .email("graysonm@test.com")
                .specialization("Cardiologist")
                .rating(BigDecimal.valueOf(0.0))
                .doctorStatus(DoctorStatus.ACTIVE)
                .build();

        this.doctorResponseDTO = DoctorResponseDTO.builder()
                .id(doctorId.toString())
                .firstName("Mark")
                .lastName("Grayson")
                .gender(Genders.MALE.toString())
                .email("graysonm@test.com")
                .phoneNumber("13324453")
                .specialization("Cardiologist")
                .rating(BigDecimal.valueOf(0.0).toString())
                .doctorStatus(DoctorStatus.ACTIVE.toString())
                .build();
    }

    @Test
    void toResponseDTOShouldSuccessfullyMap() {
        DoctorResponseDTO doctorResponseDTO = DoctorMapper.toResponseDTO(doctor);

        assertEquals(doctorResponseDTO.getId(), doctorId.toString());
        assertEquals(doctorResponseDTO.getFirstName(), "Mark");
        assertEquals(doctorResponseDTO.getLastName(), "Grayson");
        assertEquals(doctorResponseDTO.getGender(), Genders.MALE.toString());
        assertEquals(doctorResponseDTO.getPhoneNumber(), "+13324453");
    }

    @Test
    void toModelShouldSuccessfullyMap() {
        Doctor mappedDoctor = DoctorMapper.toModel(doctorRequestDTO);

        assertEquals(mappedDoctor.getFirstName(), "Mark");
        assertEquals(mappedDoctor.getLastName(), "Grayson");
        assertEquals(mappedDoctor.getGender(), Genders.MALE);
        assertEquals(mappedDoctor.getPhoneNumber(), "13324453");
    }
}