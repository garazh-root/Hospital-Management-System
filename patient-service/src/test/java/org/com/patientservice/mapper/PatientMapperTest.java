package org.com.patientservice.mapper;

import org.com.patientservice.additional.PatientStatus;
import org.com.patientservice.additional.Roles;
import org.com.patientservice.dto.PatientResponseDTO;
import org.com.patientservice.events.UserRegisteredEvent;
import org.com.patientservice.exception.EmptyComponentException;
import org.com.patientservice.exception.EmptyEntityException;
import org.com.patientservice.model.Patient;
import org.com.patientservice.model.genders.Gender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class PatientMapperTest {

    PatientMapper patientMapper;

    @Test
    void toDTOShouldMapPatientToDTO() {
        Patient patient = Patient.builder()
                .patientId(UUID.fromString("bcfe825a-700b-488e-8955-50bd5887b9c3"))
                .firstName("John")
                .lastName("Doe")
                .gender(Gender.MALE)
                .weight(80.3)
                .height(180.5)
                .email("johndoe@exapmle.com")
                .phoneNumber("+1 (234) 567890")
                .dateOfBirth(LocalDate.parse("2001-03-09"))
                .address("Test Address")
                .patientStatus(PatientStatus.ACTIVE)
                .registeredDate(LocalDate.now())
                .build();

        PatientResponseDTO response = PatientMapper.toDTO(patient);

        assertThat(response.getId()).isEqualTo(patient.getPatientId().toString());
        assertThat(response.getFirstName()).isEqualTo(patient.getFirstName());
        assertThat(response.getLastName()).isEqualTo(patient.getLastName());
        assertThat(response.getWeight()).isEqualTo(patient.getWeight().toString());
        assertThat(response.getHeight()).isEqualTo(patient.getHeight().toString());
        assertThat(response.getEmail()).isEqualTo(patient.getEmail());
        assertThat(response.getPhoneNumber()).isEqualTo(patient.getPhoneNumber());
        assertThat(response.getDateOfBirth()).isEqualTo(patient.getDateOfBirth().toString());
        assertThat(response.getAddress()).isEqualTo(patient.getAddress());
        assertThat(response.getStatus()).isEqualTo(patient.getPatientStatus().toString());
    }

    @Test
    void toDTOShouldThrowExceptionWhenNullPatient(){
        Assertions.assertThrows(EmptyEntityException.class, ()  -> PatientMapper.toDTO(null));
    }

    @Test
    void toModelShouldMapPatientRequestToModel() {
        UUID patientId = UUID.randomUUID();

        UserRegisteredEvent event = new UserRegisteredEvent(
                patientId, "username", "Jack", "Simpson", "test@mail.com",
                "+1 492 44 224", Roles.PATIENT, LocalDateTime.now()
        );

        Patient patient = patientMapper.toModel(event);

        assertThat(event.id()).isEqualTo(patient.getPatientId());
        assertThat(event.firstName()).isEqualTo(patient.getFirstName());
        assertThat(event.lastName()).isEqualTo(patient.getLastName());
        assertThat(event.email()).isEqualTo(patient.getEmail());
    }

    @Test
    void toModelShouldThrowExceptionWhenPatientRequestIsNull(){
        Assertions.assertThrows(EmptyComponentException.class, ()  -> PatientMapper.toModel(null));
    }

    @Test
    void toModelShouldThrowExceptionWhenPatientIsEmpty() {
        Assertions.assertThrows(EmptyComponentException.class, ()  -> PatientMapper.toModel(null));
    }
}