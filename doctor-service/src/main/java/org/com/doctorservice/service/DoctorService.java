package org.com.doctorservice.service;

import lombok.RequiredArgsConstructor;
import org.com.doctorservice.dto.DoctorRequestDTO;
import org.com.doctorservice.dto.DoctorResponseDTO;
import org.com.doctorservice.exception.DoctorNotFoundException;
import org.com.doctorservice.exception.EmailAlreadyExistsException;
import org.com.doctorservice.kafka.KafkaProducer;
import org.com.doctorservice.mapper.DoctorMapper;
import org.com.doctorservice.messages.DoctorServiceMessages;
import org.com.doctorservice.model.Doctor;
import org.com.doctorservice.additional.Genders;
import org.com.doctorservice.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
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

    public DoctorResponseDTO createDoctor(DoctorRequestDTO doctorRequestDTO){
        if(doctorRepository.existsByEmail(doctorRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException(DoctorServiceMessages.EMAIL_ALREADY_EXISTS.getMessage());
        }

        Doctor doctor = DoctorMapper.toModel(doctorRequestDTO);

        Doctor savedDoctor = doctorRepository.save(doctor);

        DoctorResponseDTO response = DoctorMapper.toResponseDTO(savedDoctor);
        kafkaProducer.sendDoctorCreated(response);

        return response;
    }

    public DoctorResponseDTO updateDoctor(UUID id, DoctorRequestDTO request){
        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new DoctorNotFoundException(id.toString()));

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
        kafkaProducer.sendDoctorUpdated(response);

        return response;
    }

    public void deleteDoctor(UUID id){
        doctorRepository.deleteById(id);
        kafkaProducer.sendDoctorDeleted(id);
    }

    private List<DoctorResponseDTO> mapList(List<Doctor> doctors){
        return doctors.stream()
                .map(DoctorMapper::toResponseDTO)
                .toList();
    }
}