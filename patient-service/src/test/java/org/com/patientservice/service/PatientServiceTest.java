//package org.com.patientservice.service;
//
//import org.com.patientservice.additional.PatientStatus;
//import org.com.patientservice.dto.PatientRequestDTO;
//import org.com.patientservice.dto.PatientResponseDTO;
//import org.com.patientservice.exception.EmailAlreadyExistsException;
//import org.com.patientservice.exception.PatientNotFoundException;
//import org.com.patientservice.kafka.KafkaProducer;
//import org.com.patientservice.model.Patient;
//import org.com.patientservice.model.genders.Gender;
//import org.com.patientservice.repository.PatientRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
//public class PatientServiceTest {
//
//    @Mock
//    PatientRepository patientRepository;
//
//    @Mock
//    KafkaProducer kafkaProducer;
//
//    @InjectMocks
//    PatientService patientService;
//
//    @Test
//    public void getPatientShouldReturnListOfPatients() {
//
//        Patient firstPatient = Patient.builder()
//                .patientId(UUID.randomUUID())
//                .firstName("Emily")
//                .lastName("Chomenko")
//                .gender(Gender.MALE)
//                .weight(80.3)
//                .height(180.5)
//                .email("testmail@test.com")
//                .phoneNumber("+1 (380) 223 98 83")
//                .dateOfBirth(LocalDate.parse("2004-09-01"))
//                .address("Test Street 23/3")
//                .patientStatus(PatientStatus.ACTIVE)
//                .registeredDate(LocalDate.now())
//                .build();
//
//        Patient secondPatient = Patient.builder()
//                .patientId(UUID.randomUUID())
//                .firstName("David")
//                .lastName("Chomenko")
//                .gender(Gender.MALE)
//                .weight(80.3)
//                .height(180.5)
//                .email("testmail@test.com")
//                .phoneNumber("+1 (380) 223 98 83")
//                .dateOfBirth(LocalDate.parse("2009-09-03"))
//                .address("Test Block 23/3")
//                .patientStatus(PatientStatus.ACTIVE)
//                .registeredDate(LocalDate.now())
//                .build();
//
//        when(patientRepository.findAll()).thenReturn(List.of(firstPatient, secondPatient));
//
//        List<PatientResponseDTO> response = patientService.getPatients();
//
//        assertThat(response.size()).isEqualTo(2);
//        assertThat(response.get(0).getId()).isEqualTo(firstPatient.getPatientId().toString());
//        assertThat(response.get(0).getFirstName()).isEqualTo(firstPatient.getFirstName());
//        assertThat(response.get(0).getLastName()).isEqualTo(firstPatient.getLastName());
//        assertThat(response.get(0).getGender()).isEqualTo(firstPatient.getGender().toString());
//        assertThat(response.get(0).getWeight()).isEqualTo(firstPatient.getWeight().toString());
//        assertThat(response.get(0).getHeight()).isEqualTo(firstPatient.getHeight().toString());
//        assertThat(response.get(0).getEmail()).isEqualTo(firstPatient.getEmail());
//        assertThat(response.get(0).getPhoneNumber()).isEqualTo(firstPatient.getPhoneNumber());
//        assertThat(response.get(0).getDateOfBirth()).isEqualTo(firstPatient.getDateOfBirth().toString());
//        assertThat(response.get(0).getAddress()).isEqualTo(firstPatient.getAddress());
//        assertThat(response.get(0).getStatus()).isEqualTo(firstPatient.getPatientStatus().toString());
//
//        assertThat(response.get(1).getId()).isEqualTo(secondPatient.getPatientId().toString());
//        assertThat(response.get(1).getFirstName()).isEqualTo(secondPatient.getFirstName());
//        assertThat(response.get(1).getLastName()).isEqualTo(secondPatient.getLastName());
//        assertThat(response.get(1).getGender()).isEqualTo(secondPatient.getGender().toString());
//        assertThat(response.get(1).getWeight()).isEqualTo(secondPatient.getWeight().toString());
//        assertThat(response.get(1).getHeight()).isEqualTo(secondPatient.getHeight().toString());
//        assertThat(response.get(1).getEmail()).isEqualTo(secondPatient.getEmail());
//        assertThat(response.get(1).getPhoneNumber()).isEqualTo(secondPatient.getPhoneNumber());
//        assertThat(response.get(1).getDateOfBirth()).isEqualTo(secondPatient.getDateOfBirth().toString());
//        assertThat(response.get(1).getAddress()).isEqualTo(secondPatient.getAddress());
//        assertThat(response.get(1).getStatus()).isEqualTo(secondPatient.getPatientStatus().toString());
//
//        verify(patientRepository).findAll();
//        verifyNoMoreInteractions(patientRepository);
//    }
//
//    @Test
//    void getPatientShouldReturnEmptyListWhenNoPatientFound() {
//        when(patientRepository.findAll()).thenReturn(List.of());
//
//        List<PatientResponseDTO> responseList = patientService.getPatients();
//
//        assertThat(responseList.size()).isEqualTo(0);
//
//        verify(patientRepository).findAll();
//        verifyNoMoreInteractions(patientRepository);
//    }
//
//    @Test
//    void getPatientByIdShouldReturnPatient() {
//        UUID patientId = UUID.randomUUID();
//
//        Patient patient = Patient.builder()
//                .patientId(patientId)
//                .firstName("David")
//                .lastName("Chomenko")
//                .gender(Gender.MALE)
//                .weight(80.3)
//                .height(180.5)
//                .email("testmail@test.com")
//                .phoneNumber("+1 (380) 223 98 83")
//                .dateOfBirth(LocalDate.parse("2009-09-03"))
//                .address("Test Block 23/3")
//                .patientStatus(PatientStatus.ACTIVE)
//                .registeredDate(LocalDate.parse("2020-01-01"))
//                .build();
//
//        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));
//
//        PatientResponseDTO response = patientService.getPatientById(patientId);
//
//        assertThat(response.getFirstName()).isEqualTo(patient.getFirstName());
//        assertThat(response.getLastName()).isEqualTo(patient.getLastName());
//        assertThat(response.getEmail()).isEqualTo(patient.getEmail());
//        assertThat(response.getGender()).isEqualTo(patient.getGender().toString());
//        assertThat(response.getWeight()).isEqualTo(patient.getWeight().toString());
//        assertThat(response.getHeight()).isEqualTo(patient.getHeight().toString());
//        assertThat(response.getEmail()).isEqualTo(patient.getEmail());
//        assertThat(response.getPhoneNumber()).isEqualTo(patient.getPhoneNumber());
//        assertThat(response.getDateOfBirth()).isEqualTo(patient.getDateOfBirth().toString());
//        assertThat(response.getAddress()).isEqualTo(patient.getAddress());
//        assertThat(response.getStatus()).isEqualTo(patient.getPatientStatus().toString());
//
//        verify(patientRepository).findById(patientId);
//        verifyNoMoreInteractions(patientRepository);
//    }
//
//    @Test
//    void getPatientByIdShouldReturnEmptyPatientWhenNoPatientFound() {
//        UUID patientId = UUID.randomUUID();
//
//        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> patientService.getPatientById(patientId)).isInstanceOf(NoSuchElementException.class);
//
//        verify(patientRepository).findById(patientId);
//        verifyNoMoreInteractions(patientRepository);
//    }
//
//    @Test
//    void createPatientShouldReturnPatient() {
//
//        PatientRequestDTO request = PatientRequestDTO.builder()
//                .firstName("David")
//                .lastName("Chomenko")
//                .gender(Gender.MALE)
//                .weight(80.3)
//                .height(180.5)
//                .email("testmail@test.com")
//                .phoneNumber("+1 (380) 223 98 83")
//                .dateOfBirth(LocalDate.parse("2009-09-03"))
//                .address("Test Block 23/3")
//                .registeredDate(LocalDate.parse("2009-09-03"))
//                .build();
//
//
//        Patient patient = Patient.builder()
//                .patientId(UUID.randomUUID())
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .gender(request.getGender())
//                .weight(request.getWeight())
//                .height(request.getHeight())
//                .email(request.getEmail())
//                .phoneNumber(request.getPhoneNumber())
//                .dateOfBirth(request.getDateOfBirth())
//                .address(request.getAddress())
//                .registeredDate(request.getRegisteredDate())
//                .patientStatus(PatientStatus.ACTIVE)
//                .build();
//
//        when(patientRepository.existsByEmail(request.getEmail())).thenReturn(false);
//        when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(patient);
//
//        PatientResponseDTO response = patientService.createPatient(request);
//
//        assertThat(response.getFirstName()).isEqualTo(patient.getFirstName());
//        assertThat(response.getLastName()).isEqualTo(patient.getLastName());
//        assertThat(response.getGender()).isEqualTo(patient.getGender().toString());
//        assertThat(response.getWeight()).isEqualTo(patient.getWeight().toString());
//        assertThat(response.getHeight()).isEqualTo(patient.getHeight().toString());
//        assertThat(response.getEmail()).isEqualTo(patient.getEmail());
//        assertThat(response.getPhoneNumber()).isEqualTo(patient.getPhoneNumber());
//        assertThat(response.getDateOfBirth()).isEqualTo(patient.getDateOfBirth().toString());
//        assertThat(response.getAddress()).isEqualTo(patient.getAddress());
//        assertThat(response.getStatus()).isEqualTo(patient.getPatientStatus().toString());
//
//        verify(patientRepository).existsByEmail(request.getEmail());
//        verify(patientRepository).save(Mockito.any(Patient.class));
//        verifyNoMoreInteractions(patientRepository);
//    }
//
//    @Test
//    void createPatientShouldThrowEmptyEntityExceptionWhenPatientExistsWithEmail() {
//        PatientRequestDTO request = PatientRequestDTO.builder()
//                .firstName("David")
//                .lastName("Chomenko")
//                .gender(Gender.MALE)
//                .weight(80.5)
//                .height(180.5)
//                .email("testmail@test.com")
//                .phoneNumber("+1 (380) 223 98 83")
//                .dateOfBirth(LocalDate.parse("2009-09-03"))
//                .address("Test Block 23/3")
//                .registeredDate(LocalDate.parse("2009-09-03"))
//                .build();
//
//        when(patientRepository.existsByEmail(request.getEmail())).thenReturn(true);
//
//        assertThrows(EmailAlreadyExistsException.class, () -> patientService.createPatient(request));
//
//        verify(patientRepository).existsByEmail(request.getEmail());
//        verifyNoMoreInteractions(patientRepository);
//    }
//
//    @Test
//    void updatePatientShouldReturnUpdatedPatient() {
//        when(patientRepository.findById(Mockito.any())).thenReturn(Optional.of(new Patient()));
//
//        UUID patientId = UUID.randomUUID();
//
//        PatientRequestDTO request = PatientRequestDTO.builder()
//                .firstName("David")
//                .lastName("Chomenko")
//                .gender(Gender.MALE)
//                .weight(80.5)
//                .height(185.5)
//                .email("testmail@test.com")
//                .phoneNumber("+1 (380) 223 98 83")
//                .dateOfBirth(LocalDate.parse("2009-09-03"))
//                .address("Test Block 23/3")
//                .registeredDate(LocalDate.parse("2009-09-03"))
//                .build();
//
//        when(patientRepository.existsByEmailAndPatientIdNot(request.getEmail(), patientId)).thenReturn(true);
//
//        Patient patient = Patient.builder()
//                .patientId(patientId)
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .gender(request.getGender())
//                .weight(request.getWeight())
//                .height(request.getHeight())
//                .email(request.getEmail())
//                .phoneNumber(request.getPhoneNumber())
//                .dateOfBirth(request.getDateOfBirth())
//                .address(request.getAddress())
//                .patientStatus(PatientStatus.ACTIVE)
//                .registeredDate(request.getRegisteredDate())
//                .build();
//
//        when(patientRepository.save(Mockito.any(Patient.class))).thenReturn(patient);
//
//        PatientResponseDTO response = patientService.updatePatient(patientId, request);
//
//        assertThat(response.getFirstName()).isEqualTo(patient.getFirstName());
//        assertThat(response.getLastName()).isEqualTo(patient.getLastName());
//        assertThat(response.getGender()).isEqualTo(patient.getGender().toString());
//        assertThat(response.getWeight()).isEqualTo(patient.getWeight().toString());
//        assertThat(response.getHeight()).isEqualTo(patient.getHeight().toString());
//        assertThat(response.getEmail()).isEqualTo(patient.getEmail());
//        assertThat(response.getPhoneNumber()).isEqualTo(patient.getPhoneNumber());
//        assertThat(response.getDateOfBirth()).isEqualTo(patient.getDateOfBirth().toString());
//        assertThat(response.getAddress()).isEqualTo(patient.getAddress());
//        assertThat(response.getStatus()).isEqualTo(patient.getPatientStatus().toString());
//
//        verify(patientRepository).findById(patientId);
//        verify(patientRepository).save(Mockito.any(Patient.class));
//
//    }
//
//    @Test
//    void updatePatientShouldThrowPatientNotFoundExceptionWhenPatientNotFound() {
//        when(patientRepository.findById(Mockito.any())).thenReturn(Optional.empty());
//
//        assertThrows(PatientNotFoundException.class, () -> patientService.updatePatient(UUID.randomUUID(), PatientRequestDTO.builder().build()));
//
//        verify(patientRepository).findById(Mockito.any());
//        verifyNoMoreInteractions(patientRepository);
//    }
//
//    @Test
//    void updatePatientShouldThrowEmailAlreadyExistExceptionWhenEmailAlreadyExists() {
//        UUID patientId = UUID.randomUUID();
//
//        Patient patient = Patient.builder()
//                .patientId(patientId)
//                .firstName("David")
//                .lastName("Chomenko")
//                .gender(Enum.valueOf(Gender.class, Gender.MALE.toString()))
//                .weight(80.3)
//                .height(185.5)
//                .email("testmail@test.com")
//                .phoneNumber("+1 380 223 98 83")
//                .dateOfBirth(LocalDate.parse("2009-09-03"))
//                .address("Test Block 23/3")
//                .registeredDate(LocalDate.parse("2020-01-01"))
//                .build();
//
//        PatientRequestDTO request = PatientRequestDTO.builder()
//                .firstName("David")
//                .lastName("Chomenko")
//                .gender(Gender.MALE)
//                .weight(80.3)
//                .height(185.5)
//                .email("test2mail@test.com")
//                .phoneNumber("+1 (380) 223 98 83")
//                .dateOfBirth(LocalDate.parse("2009-09-03"))
//                .address("Test Block 23/3")
//                .registeredDate(LocalDate.parse("2020-01-01"))
//                .build();
//
//        when(patientRepository.findById(Mockito.any())).thenReturn(Optional.of(patient));
//        when(patientRepository.existsByEmailAndPatientIdNot(patient.getEmail(), patientId)).thenReturn(true);
//
//        assertThrows(EmailAlreadyExistsException.class, () -> patientService.updatePatient(patientId, request));
//
//        verify(patientRepository).findById(patientId);
//        verify(patientRepository).existsByEmailAndPatientIdNot(patient.getEmail(), patientId);
//        verifyNoMoreInteractions(patientRepository);
//    }
//
//    @Test
//    void deletePatientShouldDeletePatient() {
//        UUID patientId = UUID.randomUUID();
//
//        patientService.deletePatient(patientId);
//
//        verify(patientRepository, times(1)).deleteById(patientId);
//        verifyNoMoreInteractions(patientRepository);
//    }
//}