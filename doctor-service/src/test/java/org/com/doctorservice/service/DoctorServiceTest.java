package org.com.doctorservice.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.com.doctorservice.additional.DoctorStatus;
import org.com.doctorservice.dto.DoctorRequestDTO;
import org.com.doctorservice.dto.DoctorResponseDTO;
import org.com.doctorservice.exception.DoctorNotFoundException;
import org.com.doctorservice.exception.EmailAlreadyExistsException;
import org.com.doctorservice.kafka.KafkaProducer;
import org.com.doctorservice.model.Doctor;
import org.com.doctorservice.additional.Genders;
import org.com.doctorservice.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private DoctorService doctorService;

    private DoctorResponseDTO doctorResponseDTO;
    private Doctor doctor;
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
    void getDoctorsShouldSuccessFullyReturnListOfDoctors() {
        List<Doctor> doctorsList = List.of(doctor);

        when(doctorRepository.findAll()).thenReturn(doctorsList);

        List<DoctorResponseDTO> doctorResponseDTOs = doctorService.getDoctors();

        Assertions.assertNotNull(doctorResponseDTOs);
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void getDoctorsShouldReturnEmptyListWhenDoctorsNotFound() {
        when(doctorRepository.findAll()).thenReturn(Collections.emptyList());

        List<DoctorResponseDTO> doctorResponseDTOs = doctorService.getDoctors();

        Assertions.assertTrue(doctorResponseDTOs.isEmpty());
        verify(doctorRepository, times(1)).findAll();
    }

    @Test
    void getDoctorByIdShouldReturnDoctorResponse () {
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        DoctorResponseDTO doctorResponseDTO = doctorService.getDoctorById(doctorId);

        Assertions.assertNotNull(doctorResponseDTO);
        verify(doctorRepository, times(1)).findById(doctorId);
    }

    @Test
    void getDoctorByIdShouldThrowExceptionWhenDoctorNotFound() {
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        Assertions.assertThrows(DoctorNotFoundException.class, () -> doctorService.getDoctorById(doctorId));

        verify(doctorRepository, times(1)).findById(doctorId);
    }

    @Test
    void getDoctorsBySpecializationShouldReturnListOfDoctors() {
        List<Doctor> doctorsList = List.of(doctor);

        when(doctorRepository.findBySpecialization("Cardiologist")).thenReturn(doctorsList);

        List<DoctorResponseDTO> responseDTOList = doctorService.getAllDoctorsBySpecialization("Cardiologist");

        Assertions.assertNotNull(responseDTOList);
        verify(doctorRepository, times(1)).findBySpecialization("Cardiologist");
    }

    @Test
    void getDoctorsBySpecializationShouldThrowExceptionWhenDoctorNotFound() {
        when(doctorRepository.findBySpecialization("Cardiologist")).thenReturn(Collections.emptyList());

        List<DoctorResponseDTO> doctorResponseDTOList = doctorService.getAllDoctorsBySpecialization("Cardiologist");

        Assertions.assertTrue(doctorResponseDTOList.isEmpty());
        verify(doctorRepository, times(1)).findBySpecialization("Cardiologist");
    }

    @Test
    void getDoctorsByGenderShouldReturnListOfDoctors() {
        List<Doctor> doctorsList = List.of(doctor);

        when(doctorRepository.findDoctorsByGender(Genders.MALE)).thenReturn(doctorsList);

        List<DoctorResponseDTO> doctorResponseDTOS = doctorService.findAllDoctorsByGender(Genders.MALE);

        Assertions.assertNotNull(doctorResponseDTOS);
        verify(doctorRepository, times(1)).findDoctorsByGender(Genders.MALE);
    }

    @Test
    void getDoctorsByGenderShouldReturnEmptyListWhenDoctorsNotFound() {
        when(doctorRepository.findDoctorsByGender(Genders.MALE)).thenReturn(Collections.emptyList());

        List<DoctorResponseDTO> responseDTOS = doctorService.findAllDoctorsByGender(Genders.MALE);

        Assertions.assertTrue(responseDTOS.isEmpty());
        verify(doctorRepository, times(1)).findDoctorsByGender(Genders.MALE);
    }

    @Test
    void createDoctorShouldSuccessfullyCreateDoctorResponse() {
        when(doctorRepository.existsById(doctorId)).thenReturn(true);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        DoctorResponseDTO response = doctorService.createDoctor(doctorRequestDTO);

        Assertions.assertNotNull(response);

        assertEquals(response.getId(), doctorId.toString());
        assertEquals(response.getFirstName(), "Mark");
        assertEquals(response.getLastName(), "Grayson");
        assertEquals(response.getGender(), Genders.MALE.toString());
        assertEquals(response.getPhoneNumber(), "+13324453");
        assertEquals(response.getSpecialization(), "Cardiologist");
        assertEquals(response.getRating(), BigDecimal.valueOf(0.0).toString());

        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void createDoctorShouldThrowExceptionWhenEmailAlreadyExists() {
        when(doctorRepository.existsByEmail(doctorRequestDTO.getEmail())).thenReturn(true);

        Assertions.assertThrows(EmailAlreadyExistsException.class, () -> doctorService.createDoctor(doctorRequestDTO));

        verify(doctorRepository, times(1)).existsByEmail(doctorRequestDTO.getEmail());
    }

    @Test
    void updateDoctorShouldSuccessfullyUpdateDoctorModel() {
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(doctorRepository.existsByEmailAndDoctorIdNot(doctor.getEmail(), doctorId)).thenReturn(false);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        DoctorResponseDTO response = doctorService.updateDoctor(doctorId, doctorRequestDTO);

        Assertions.assertNotNull(response);

        assertEquals(response.getId(), doctorId.toString());
        assertEquals(response.getFirstName(), "Mark");
        assertEquals(response.getLastName(), "Grayson");
        assertEquals(response.getGender(), Genders.MALE.toString());
        assertEquals(response.getPhoneNumber(), "13324453");
        assertEquals(response.getSpecialization(), "Cardiologist");

        verify(doctorRepository, times(1)).findById(doctorId);
        verify(doctorRepository, times(1)).existsByEmailAndDoctorIdNot(doctor.getEmail(), doctorId);
        verify(doctorRepository).save(any(Doctor.class));
    }

    @Test
    void updateDoctorShouldThrowExceptionWhenEmailAlreadyExists() {
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        Assertions.assertThrows(DoctorNotFoundException.class, () -> doctorService.updateDoctor(doctorId, doctorRequestDTO));

        verify(doctorRepository, times(1)).findById(doctorId);
    }

    @Test
    void deleteDoctorByIdShouldSuccessfullyDeleteDoctor() {
        doNothing().when(doctorRepository).deleteById(doctorId);

        doctorService.deleteDoctor(doctorId);

        verify(doctorRepository, times(1)).deleteById(doctorId);
    }
}