package org.com.patientservice.mapper;

import lombok.extern.slf4j.Slf4j;
import org.com.patientservice.additional.PatientStatus;
import org.com.patientservice.dto.PatientRequestDTO;
import org.com.patientservice.dto.PatientResponseDTO;
import org.com.patientservice.events.UserRegisteredEvent;
import org.com.patientservice.exception.EmptyComponentException;
import org.com.patientservice.exception.EmptyEntityException;
import org.com.patientservice.messages.PatientMessages;
import org.com.patientservice.model.Patient;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient patient) {
        if (patient == null) {
            throw new EmptyEntityException(PatientMessages.MODEL_EMPTY.getMessage());
        }

        PatientResponseDTO patientResponseDTO = PatientResponseDTO.builder()
                .id(patient.getPatientId().toString())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .gender(patient.getGender().toString())
                .weight(patient.getWeight().toString())
                .height(patient.getHeight().toString())
                .email(patient.getEmail())
                .phoneNumber(patient.getPhoneNumber())
                .dateOfBirth(patient.getDateOfBirth().toString())
                .status(patient.getPatientStatus().toString())
                .address(patient.getAddress())
                .build();

        return patientResponseDTO;
    }

    public static Patient toModel(UserRegisteredEvent userRegisteredEvent) {
        if (userRegisteredEvent == null) {
            throw new EmptyComponentException(PatientMessages.REQUEST_EMPTY.getMessage());
        }

        Patient patient = Patient.builder()
                .patientId(userRegisteredEvent.id())
                .firstName(userRegisteredEvent.firstName())
                .lastName(userRegisteredEvent.lastName())
                .email(userRegisteredEvent.email())
                .phoneNumber(userRegisteredEvent.phoneNumber())
                .patientStatus(PatientStatus.PENDING)
                .registeredDate(userRegisteredEvent.registeredAt().toLocalDate())
                .build();

        return patient;
    }
}