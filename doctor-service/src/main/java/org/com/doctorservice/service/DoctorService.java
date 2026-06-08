package org.com.doctorservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.doctorservice.additional.DoctorStatus;
import org.com.doctorservice.additional.Roles;
import org.com.doctorservice.dto.DoctorCompleteDTO;
import org.com.doctorservice.dto.DoctorRequestDTO;
import org.com.doctorservice.dto.DoctorResponseDTO;
import org.com.doctorservice.events.DoctorChangedStatusEvent;
import org.com.doctorservice.events.DoctorCreatedEvent;
import org.com.doctorservice.events.UserRegisteredEvent;
import org.com.doctorservice.exception.DoctorNotFoundException;
import org.com.doctorservice.exception.EmailAlreadyExistsException;
import org.com.doctorservice.kafka.KafkaProducer;
import org.com.doctorservice.mapper.DoctorMapper;
import org.com.doctorservice.messages.DoctorServiceMessages;
import org.com.doctorservice.model.Doctor;
import org.com.doctorservice.additional.Genders;
import org.com.doctorservice.repository.DoctorRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final KafkaProducer kafkaProducer;

    public List<DoctorResponseDTO> getDoctors(){
        return mapList(doctorRepository.findAll());
    }

    public DoctorResponseDTO getDoctorById(UUID doctorId){
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new DoctorNotFoundException(doctorId.toString()));

        return DoctorMapper.toResponseDTO(doctor);
    }

    public List<DoctorResponseDTO> getAllDoctorsBySpecialization(String specialization){
        return mapList(doctorRepository.findBySpecialization(specialization));
    }

    public List<DoctorResponseDTO> findAllDoctorsByGender(Genders gender) {
        return mapList(doctorRepository.findDoctorsByGender(gender));
    }

    @KafkaListener(topics = "user-topic", groupId = "doctor")
    public void createDoctor(UserRegisteredEvent userRegisteredEvent) {

        if(!userRegisteredEvent.role().equals(Roles.DOCTOR)){
            log.info("Doctor cannot interact with doctor service with role : {}",  userRegisteredEvent.role());
            return;
        }

        if(doctorRepository.existsByEmail(userRegisteredEvent.email())){
            log.info("User with email {} already exists", userRegisteredEvent.email());
            return;
        }

        Doctor doctor = DoctorMapper.toModel(userRegisteredEvent);

        Doctor savedDoctor = doctorRepository.save(doctor);

        DoctorResponseDTO response = DoctorMapper.toResponseDTO(savedDoctor);

        DoctorCreatedEvent createdEvent = new DoctorCreatedEvent(
                UUID.fromString(response.getId()),
                response.getFirstName(),
                response.getLastName(),
                response.getEmail(),
                response.getPhoneNumber(),
                DoctorStatus.valueOf(response.getDoctorStatus())
        );

        kafkaProducer.sendDoctorCreated(createdEvent);
    }

    public DoctorResponseDTO completeDoctor(UUID id, DoctorCompleteDTO doctorCompleteDTO){
        Doctor doctor = doctorRepository.findById(id).orElseThrow(
                () -> new DoctorNotFoundException(DoctorServiceMessages.DOCTOR_NOT_FOUND.name()));

        doctor.setGender(doctorCompleteDTO.gender());
        doctor.setSpecialization(doctorCompleteDTO.specialization());
        doctor.setRating(doctorCompleteDTO.rating());
        doctor.setDoctorStatus(DoctorStatus.ACTIVE);

        return DoctorMapper.toResponseDTO(doctorRepository.save(doctor));
    }

    public DoctorResponseDTO updateDoctor(UUID id, DoctorRequestDTO request){
        Doctor doctor = doctorRepository.findById(id).orElseThrow(
                () -> new DoctorNotFoundException(DoctorServiceMessages.DOCTOR_NOT_FOUND.name()));

        if(doctorRepository.existsByEmailAndDoctorIdNot(request.getEmail(), id)){
            throw new EmailAlreadyExistsException(DoctorServiceMessages.EMAIL_ALREADY_EXISTS.getMessage());
        }

        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setEmail(request.getEmail());
        doctor.setPhoneNumber(request.getPhoneNumber());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setDoctorStatus(request.getDoctorStatus());

        Doctor updatedDoctor = doctorRepository.save(doctor);

        DoctorResponseDTO response =  DoctorMapper.toResponseDTO(updatedDoctor);

        return response;
    }

    public void deleteDoctor(UUID id){
        doctorRepository.deleteById(id);
    }

    public DoctorResponseDTO changeDoctorStatus(UUID id, DoctorStatus newDoctorStatus){
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id.toString()));

        DoctorStatus oldStatus = doctor.getDoctorStatus();

        if(oldStatus == newDoctorStatus){
            return DoctorMapper.toResponseDTO(doctor);
        }

        doctor.setDoctorStatus(newDoctorStatus);

        doctorRepository.save(doctor);

        DoctorChangedStatusEvent changedStatusEvent = new DoctorChangedStatusEvent(
                doctor.getDoctorId(),
                oldStatus,
                newDoctorStatus
        );

        kafkaProducer.sendPatientStatusUpdated(changedStatusEvent);

        return DoctorMapper.toResponseDTO(doctor);
    }

    public List<DoctorResponseDTO> findTopRatedDoctors(int limit) {
        return doctorRepository.findAllByOrderByRatingDesc(PageRequest.of(0, limit))
                .stream()
                .map(DoctorMapper::toResponseDTO)
                .toList();
    }

    private List<DoctorResponseDTO> mapList(List<Doctor> doctors){
        return doctors.stream()
                .map(DoctorMapper::toResponseDTO)
                .toList();
    }
}