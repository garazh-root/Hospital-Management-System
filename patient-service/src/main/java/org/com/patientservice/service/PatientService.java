package org.com.patientservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.patientservice.additional.PatientStatus;
import org.com.patientservice.dto.PatientCompleteDTO;
import org.com.patientservice.dto.PatientRequestDTO;
import org.com.patientservice.dto.PatientResponseDTO;
import org.com.patientservice.events.PatientCreatedEvent;
import org.com.patientservice.events.PatientStatusUpdatedEvent;
import org.com.patientservice.events.UserRegisteredEvent;
import org.com.patientservice.exception.EmailAlreadyExistsException;
import org.com.patientservice.exception.PatientNotFoundException;
import org.com.patientservice.kafka.KafkaProducer;
import org.com.patientservice.mapper.PatientMapper;
import org.com.patientservice.messages.PatientMessages;
import org.com.patientservice.model.Patient;
import org.com.patientservice.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private static final Logger log = LoggerFactory.getLogger(PatientService.class);
    private final PatientRepository patientRepository;
    private final KafkaProducer kafkaProducer;

    public List<PatientResponseDTO> getPatients () {
        List<Patient> patientList = patientRepository.findAll();

        List<PatientResponseDTO> patientResponseDTOS = patientList.stream()
                .map(PatientMapper::toDTO)
                .toList();

        return patientResponseDTOS;
    }

    public PatientResponseDTO getPatientById (UUID id){
        Optional<Patient> patient = patientRepository.findById(id);

        return PatientMapper.toDTO(patient.get());
    }

    @KafkaListener(topics = "user-topic", groupId = "patient")
    public void handle(UserRegisteredEvent userRegisteredEvent) {
        if (!userRegisteredEvent.role().toString().equals("PATIENT")) {
            return;
        }

        if(patientRepository.existsByEmail(userRegisteredEvent.email())) {
            log.info("User with email address {} already exists", userRegisteredEvent.email());
            return;
        }

        Patient patient = patientRepository.save(PatientMapper.toModel(userRegisteredEvent));

        PatientResponseDTO response = PatientMapper.toDTO(patient);

        PatientCreatedEvent createdEvent = new PatientCreatedEvent(
                response.getId(),
                response.getFirstName(),
                response.getLastName(),
                response.getEmail(),
                response.getPhoneNumber(),
                PatientStatus.valueOf(response.getStatus())
        );

        kafkaProducer.sendPatientCreated(createdEvent);
    }

    public PatientResponseDTO completeFullPatientProfile(UUID id, PatientCompleteDTO patientCompleteDTO) {
        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException(PatientMessages.PATIENT_NOT_FOUND.getMessage())
        );

        patient.setGender(patientCompleteDTO.gender());
        patient.setWeight(patientCompleteDTO.weight());
        patient.setHeight(patientCompleteDTO.height());
        patient.setDateOfBirth(patientCompleteDTO.dateOfBirth());
        patient.setAddress(patientCompleteDTO.address());
        patient.setPatientStatus(PatientStatus.ACTIVE);

        Patient updatedPatient = patientRepository.save(patient);

        return PatientMapper.toDTO(updatedPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO request){
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(PatientMessages.PATIENT_NOT_FOUND.getMessage()));

        if(patientRepository.existsByEmailAndPatientIdNot(patient.getEmail(), patient.getPatientId())) {
            throw new EmailAlreadyExistsException(PatientMessages.EMAIL_ALREADY_EXISTS.getMessage());
        }

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setWeight(request.getWeight());
        patient.setEmail(request.getEmail());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setAddress(request.getAddress());

        Patient updatedPatient = patientRepository.save(patient);

        PatientResponseDTO response = PatientMapper.toDTO(updatedPatient);

        return PatientMapper.toDTO(updatedPatient);
    }

    public void deletePatient(UUID id){
        patientRepository.deleteById(id);
    }

    public PatientResponseDTO changePatientStatus(UUID id, PatientStatus newPatientStatus){
        Patient patient = patientRepository.findById(id).orElseThrow(() -> new PatientNotFoundException(PatientMessages.PATIENT_NOT_FOUND.getMessage()));

        PatientStatus oldPatientStatus = patient.getPatientStatus();

        if(oldPatientStatus == newPatientStatus) {
            return PatientMapper.toDTO(patient);
        }

        patient.setPatientStatus(newPatientStatus);

        patientRepository.save(patient);

        PatientStatusUpdatedEvent updatedEvent = new PatientStatusUpdatedEvent(
                patient.getPatientId(),
                oldPatientStatus,
                newPatientStatus
        );

        kafkaProducer.sendPatientStatusUpdated(updatedEvent);

        return PatientMapper.toDTO(patient);
    }
}