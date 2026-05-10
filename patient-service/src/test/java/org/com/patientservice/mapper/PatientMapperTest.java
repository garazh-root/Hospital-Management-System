//package org.com.patientservice.mapper;
//
//import org.com.patientservice.additional.PatientStatus;
//import org.com.patientservice.dto.PatientRequestDTO;
//import org.com.patientservice.dto.PatientResponseDTO;
//import org.com.patientservice.exception.EmptyComponentException;
//import org.com.patientservice.exception.EmptyEntityException;
//import org.com.patientservice.model.Patient;
//import org.com.patientservice.model.genders.Gender;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class PatientMapperTest {
//
//    PatientMapper patientMapper;
//
//    @Test
//    void toDTOShouldMapPatientToDTO() {
//        Patient patient = Patient.builder()
//                .patientId(UUID.fromString("bcfe825a-700b-488e-8955-50bd5887b9c3"))
//                .firstName("John")
//                .lastName("Doe")
//                .gender(Gender.MALE)
//                .weight(80.3)
//                .height(180.5)
//                .email("johndoe@exapmle.com")
//                .phoneNumber("+1 (234) 567890")
//                .dateOfBirth(LocalDate.parse("2001-03-09"))
//                .address("Test Address")
//                .patientStatus(PatientStatus.ACTIVE)
//                .registeredDate(LocalDate.now())
//                .build();
//
//        PatientResponseDTO response = PatientMapper.toDTO(patient);
//
//        assertThat(response.getId()).isEqualTo(patient.getPatientId().toString());
//        assertThat(response.getFirstName()).isEqualTo(patient.getFirstName());
//        assertThat(response.getLastName()).isEqualTo(patient.getLastName());
//        assertThat(response.getWeight()).isEqualTo(patient.getWeight().toString());
//        assertThat(response.getHeight()).isEqualTo(patient.getHeight().toString());
//        assertThat(response.getEmail()).isEqualTo(patient.getEmail());
//        assertThat(response.getPhoneNumber()).isEqualTo(patient.getPhoneNumber());
//        assertThat(response.getDateOfBirth()).isEqualTo(patient.getDateOfBirth().toString());
//        assertThat(response.getAddress()).isEqualTo(patient.getAddress());
//        assertThat(response.getStatus()).isEqualTo(patient.getPatientStatus().toString());
//    }
//
//    @Test
//    void toDTOShouldThrowExceptionWhenNullPatient(){
//        Patient patient =  null;
//
//        Assertions.assertThrows(EmptyEntityException.class, ()  -> PatientMapper.toDTO(patient));
//    }
//
//    @Test
//    void validateShouldThrowExceptionWhenPatientIsEmpty(){
//        Patient patient = Patient.builder()
//                .patientId(UUID.randomUUID())
//                .firstName("")
//                .lastName("Doe")
//                .gender(Gender.MALE)
//                .weight(80.3)
//                .height(180.5)
//                .email("johndoe@exapmle.com")
//                .phoneNumber("+1 (234) 567890")
//                .dateOfBirth(LocalDate.parse("2001-03-09"))
//                .address("Test Address")
//                .patientStatus(PatientStatus.ACTIVE)
//                .registeredDate(LocalDate.now())
//                .build();
//
//        Assertions.assertThrows(EmptyComponentException.class, ()  -> PatientMapper.toDTO(patient));
//
//    }
//
//    @Test
//    void toModelShouldMapPatientRequestToModel() {
//        PatientRequestDTO request = PatientRequestDTO.builder()
//                .firstName("Rocky")
//                .lastName("Balboa")
//                .gender(Gender.MALE)
//                .weight(80.3)
//                .height(180.5)
//                .email("testmail@test.com")
//                .phoneNumber("+ (111) 1111111")
//                .dateOfBirth(LocalDate.parse("1978-03-01"))
//                .address("Test Street 34")
//                .registeredDate(LocalDate.parse("2023-01-01"))
//                .build();
//
//        Patient patient = patientMapper.toModel(request);
//
//        assertThat(patient.getFirstName()).isEqualTo(request.getFirstName());
//        assertThat(patient.getLastName()).isEqualTo(request.getLastName());
//        assertThat(patient.getWeight()).isEqualTo(request.getWeight());
//        assertThat(patient.getHeight()).isEqualTo(request.getHeight());
//        assertThat(patient.getEmail()).isEqualTo(request.getEmail());
//        assertThat(patient.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
//        assertThat(patient.getDateOfBirth()).isEqualTo(request.getDateOfBirth());
//        assertThat(patient.getAddress()).isEqualTo(request.getAddress());
//        assertThat(patient.getRegisteredDate()).isEqualTo(request.getRegisteredDate());
//    }
//
//    @Test
//    void toModelShouldThrowExceptionWhenPatientRequestIsNull(){
//        PatientRequestDTO patientRequestDTO = null;
//
//        Assertions.assertThrows(EmptyComponentException.class, ()  -> PatientMapper.toModel(patientRequestDTO));
//    }
//
//    @Test
//    void toModelShouldThrowExceptionWhenPatientIsEmpty() {
//        PatientRequestDTO request = PatientRequestDTO.builder()
//                .firstName("")
//                .lastName("Balboa")
//                .gender(Gender.MALE)
//                .weight(80.3)
//                .height(180.5)
//                .email("testmail@test.com")
//                .phoneNumber("1111111111")
//                .dateOfBirth(LocalDate.parse("1973-03-01"))
//                .address("Test Street 34")
//                .registeredDate(LocalDate.parse("2023-01-01"))
//                .build();
//
//        Assertions.assertThrows(EmptyEntityException.class, ()  -> PatientMapper.toModel(request));
//    }
//}