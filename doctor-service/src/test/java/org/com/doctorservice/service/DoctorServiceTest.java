package org.com.doctorservice.service;

import org.com.doctorservice.additional.CustomDayOfTheWeek;
import org.com.doctorservice.additional.DoctorStatus;
import org.com.doctorservice.dto.DoctorRequestDTO;
import org.com.doctorservice.dto.DoctorResponseDTO;
import org.com.doctorservice.exception.DoctorNotFoundException;
import org.com.doctorservice.exception.EmailAlreadyExistsException;
import org.com.doctorservice.kafka.KafkaProducer;
import org.com.doctorservice.mapper.DoctorMapper;
import org.com.doctorservice.model.Doctor;
import org.com.doctorservice.additional.Genders;
import org.com.doctorservice.repository.DoctorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private KafkaProducer kafkaProducer;

    @InjectMocks
    private DoctorService doctorService;

    @Test
    void getDoctorsShouldReturnListOfDoctors() {
        Schedule firstScheduleForFirstDoctor = buildSchedule();

        Schedule secondScheduleForFirstDoctor = buildSchedule();
        secondScheduleForFirstDoctor.setCustomDayOfTheWeek(CustomDayOfTheWeek.TUESDAY);

        Schedule firstScheduleForSecondDoctor = buildSchedule();
        firstScheduleForSecondDoctor.setCustomDayOfTheWeek(CustomDayOfTheWeek.WEDNESDAY);

        Schedule secondScheduleForSecondDoctor = buildSchedule();
        secondScheduleForSecondDoctor.setCustomDayOfTheWeek(CustomDayOfTheWeek.FRIDAY);

        Doctor firstDoctor = Doctor.builder()
                .doctorId(UUID.randomUUID())
                .firstName("Jared")
                .lastName("Letto")
                .gender(Genders.MALE)
                .email("test@mail.ru")
                .phoneNumber("+ 1 (420) 432 45221")
                .specialization("Dentist")
                .rating(BigDecimal.valueOf(9.3))
                .doctorStatus(DoctorStatus.ACTIVE)
                .schedules(List.of(firstScheduleForFirstDoctor, secondScheduleForFirstDoctor))
                .build();

        Doctor secondDoctor = Doctor.builder()
                .doctorId(UUID.randomUUID())
                .firstName("Tracy")
                .lastName("Quin")
                .gender(Genders.FEMALE)
                .email("test3@mail.ru")
                .phoneNumber("+ 1 (420) 432 1121")
                .specialization("Pediatrician")
                .rating(BigDecimal.valueOf(9.7))
                .doctorStatus(DoctorStatus.ACTIVE)
                .schedules(List.of(firstScheduleForSecondDoctor, secondScheduleForSecondDoctor))
                .build();

        when(doctorRepository.findAll()).thenReturn(List.of(firstDoctor, secondDoctor));

        List<DoctorResponseDTO> list = doctorService.getDoctors();

        assertThat(list.size()).isEqualTo(2);

        assertThat(list.get(0).getId()).isEqualTo(firstDoctor.getDoctorId().toString());
        assertThat(list.get(0).getFirstName()).isEqualTo(firstDoctor.getFirstName());
        assertThat(list.get(0).getLastName()).isEqualTo(firstDoctor.getLastName());
        assertThat(list.get(0).getGender()).isEqualTo(firstDoctor.getGender().toString());
        assertThat(list.get(0).getEmail()).isEqualTo(firstDoctor.getEmail());
        assertThat(list.get(0).getPhoneNumber()).isEqualTo(firstDoctor.getPhoneNumber());
        assertThat(list.get(0).getScheduleList().size()).isEqualTo(2);
        assertThat(list.get(0).getScheduleList().get(0).getScheduleStartTime()).isEqualTo(firstDoctor.getSchedules().get(0).getStartTime().toString());
        assertThat(list.get(0).getScheduleList().get(1).getScheduleStartTime()).isEqualTo(firstDoctor.getSchedules().get(1).getStartTime().toString());

        assertThat(list.get(1).getId()).isEqualTo(secondDoctor.getDoctorId().toString());
        assertThat(list.get(1).getFirstName()).isEqualTo(secondDoctor.getFirstName());
        assertThat(list.get(1).getLastName()).isEqualTo(secondDoctor.getLastName());
        assertThat(list.get(1).getGender()).isEqualTo(secondDoctor.getGender().toString());
        assertThat(list.get(1).getEmail()).isEqualTo(secondDoctor.getEmail());
        assertThat(list.get(1).getPhoneNumber()).isEqualTo(secondDoctor.getPhoneNumber());
        assertThat(list.get(1).getScheduleList().get(0).getScheduleStartTime()).isEqualTo(secondDoctor.getSchedules().get(0).getStartTime().toString());
        assertThat(list.get(1).getScheduleList().get(1).getScheduleStartTime()).isEqualTo(secondDoctor.getSchedules().get(1).getStartTime().toString());

        verify(doctorRepository).findAll();
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void getDoctorsShouldReturnEmptyList() {
        when(doctorRepository.findAll()).thenReturn(List.of());

        List<DoctorResponseDTO> list = doctorService.getDoctors();

        assertThat(list.size()).isEqualTo(0);

        verify(doctorRepository).findAll();
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void getDoctorByIdShouldReturnDoctor() {
        Schedule firstSchedule = buildSchedule();

        Schedule secondSchedule = buildSchedule();
        secondSchedule.setCustomDayOfTheWeek(CustomDayOfTheWeek.FRIDAY);

        UUID doctorId = UUID.randomUUID();

        Doctor doctor = Doctor.builder()
                .doctorId(doctorId)
                .firstName("Tracy")
                .lastName("Quin")
                .gender(Genders.FEMALE)
                .email("test3@mail.ru")
                .phoneNumber("+ 1 (420) 432 1121")
                .specialization("Pediatrician")
                .rating(BigDecimal.valueOf(9.7))
                .doctorStatus(DoctorStatus.ACTIVE)
                .schedules(List.of(firstSchedule, secondSchedule))
                .build();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        DoctorResponseDTO doctorResponseDTO = doctorService.getDoctorById(doctorId);

        assertThat(doctorResponseDTO.getId()).isEqualTo(doctorId.toString());
        assertThat(doctorResponseDTO.getFirstName()).isEqualTo(doctor.getFirstName());
        assertThat(doctorResponseDTO.getLastName()).isEqualTo(doctor.getLastName());
        assertThat(doctorResponseDTO.getGender()).isEqualTo(doctor.getGender().toString());
        assertThat(doctorResponseDTO.getEmail()).isEqualTo(doctor.getEmail());
        assertThat(doctorResponseDTO.getPhoneNumber()).isEqualTo(doctor.getPhoneNumber());
        assertThat(doctorResponseDTO.getSpecialization()).isEqualTo(doctor.getSpecialization());
        assertThat(doctorResponseDTO.getRating()).isEqualTo(doctor.getRating().toString());
        assertThat(doctorResponseDTO.getDoctorStatus()).isEqualTo(doctor.getDoctorStatus().toString());
        assertThat(doctorResponseDTO.getScheduleList().size()).isEqualTo(2);
        assertThat(doctorResponseDTO.getScheduleList().get(0).getScheduleStartTime()).isEqualTo(doctor.getSchedules().get(0).getStartTime().toString());
        assertThat(doctorResponseDTO.getScheduleList().get(1).getScheduleStartTime()).isEqualTo(doctor.getSchedules().get(1).getStartTime().toString());

        verify(doctorRepository).findById(doctorId);
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void getDoctorByIdShouldThrowExceptionWhenDoctorIdIsNull() {
        UUID doctorId = UUID.randomUUID();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> doctorService.getDoctorById(doctorId)).isInstanceOf(DoctorNotFoundException.class);

        verify(doctorRepository).findById(doctorId);
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void getDoctorsBySpecializationShouldReturnListOfDoctors() {
        Schedule firstScheduleForFirstDoctor = buildSchedule();

        Schedule secondScheduleForFirstDoctor = buildSchedule();
        secondScheduleForFirstDoctor.setCustomDayOfTheWeek(CustomDayOfTheWeek.TUESDAY);

        Schedule firstScheduleForSecondDoctor = buildSchedule();
        firstScheduleForSecondDoctor.setCustomDayOfTheWeek(CustomDayOfTheWeek.WEDNESDAY);

        Schedule secondScheduleForSecondDoctor = buildSchedule();
        secondScheduleForSecondDoctor.setCustomDayOfTheWeek(CustomDayOfTheWeek.THURSDAY);

        String specialization = "Dentist";

        Doctor firstDoctor = Doctor.builder()
                .doctorId(UUID.randomUUID())
                .firstName("Anatoly")
                .lastName("Borschikov")
                .gender(Genders.MALE)
                .email("test@mail.com")
                .phoneNumber("+1 (440) 4323432")
                .specialization(specialization)
                .rating(BigDecimal.valueOf(8.3))
                .doctorStatus(DoctorStatus.ACTIVE)
                .schedules(List.of(firstScheduleForFirstDoctor, secondScheduleForFirstDoctor))
                .build();

        Doctor secondDoctor = Doctor.builder()
                .doctorId(UUID.randomUUID())
                .firstName("Emma")
                .lastName("Frost")
                .gender(Genders.FEMALE)
                .email("test2@mail.com")
                .phoneNumber("+1 (330) 4323465")
                .specialization(specialization)
                .rating(BigDecimal.valueOf(9.3))
                .doctorStatus(DoctorStatus.ACTIVE)
                .schedules(List.of(firstScheduleForSecondDoctor, secondScheduleForSecondDoctor))
                .build();

        when(doctorRepository.findBySpecialization(specialization)).thenReturn(List.of(firstDoctor, secondDoctor));

        List<DoctorResponseDTO> list = doctorService.getAllDoctorsBySpecialization(specialization);

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0).getSpecialization()).isEqualTo(firstDoctor.getSpecialization());
        assertThat(list.get(1).getSpecialization()).isEqualTo(secondDoctor.getSpecialization());

        verify(doctorRepository).findBySpecialization(specialization);
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void getDoctorsByGenderShouldReturnListOfDoctors() {
        Schedule firstScheduleForFirstDoctor = buildSchedule();

        Schedule secondScheduleForFirstDoctor = buildSchedule();
        secondScheduleForFirstDoctor.setCustomDayOfTheWeek(CustomDayOfTheWeek.TUESDAY);

        Schedule firstScheduleForSecondDoctor = buildSchedule();
        firstScheduleForSecondDoctor.setCustomDayOfTheWeek(CustomDayOfTheWeek.WEDNESDAY);

        Schedule secondScheduleForSecondDoctor = buildSchedule();
        secondScheduleForSecondDoctor.setCustomDayOfTheWeek(CustomDayOfTheWeek.THURSDAY);

        Genders gender = Genders.FEMALE;

        Doctor firstDoctor = Doctor.builder()
                .doctorId(UUID.randomUUID())
                .firstName("Evelynn")
                .lastName("Hatch")
                .gender(gender)
                .email("test@mail.com")
                .phoneNumber("+1 (239) 234419832")
                .specialization("Hepatologist")
                .rating(BigDecimal.valueOf(9.3))
                .doctorStatus(DoctorStatus.ACTIVE)
                .schedules(List.of(firstScheduleForFirstDoctor, secondScheduleForFirstDoctor))
                .build();

        Doctor secondDoctor = Doctor.builder()
                .doctorId(UUID.randomUUID())
                .firstName("Maria")
                .lastName("Wattson")
                .gender(gender)
                .email("test3@mail.com")
                .phoneNumber("+1 (005) 234419832")
                .specialization("Ophthalmologist")
                .rating(BigDecimal.valueOf(5.6))
                .doctorStatus(DoctorStatus.ACTIVE)
                .schedules(List.of(firstScheduleForSecondDoctor, secondScheduleForSecondDoctor))
                .build();

        when(doctorRepository.findDoctorsByGender(gender)).thenReturn(List.of(firstDoctor, secondDoctor));

        List<DoctorResponseDTO> responseList = doctorService.findAllDoctorsByGender(gender);

        assertThat(responseList.size()).isEqualTo(2);
        assertThat(responseList.get(0).getGender()).isEqualTo(firstDoctor.getGender().toString());
        assertThat(responseList.get(1).getGender()).isEqualTo(secondDoctor.getGender().toString());

        verify(doctorRepository).findDoctorsByGender(gender);
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void saveDoctorShouldSaveDoctorFromRequest () {

        ScheduleRequestDTO firstScheduleRequest = scheduleRequestBuilder();

        ScheduleRequestDTO secondScheduleRequest = scheduleRequestBuilder();
        secondScheduleRequest.setCustomDayOfTheWeek(CustomDayOfTheWeek.TUESDAY);

        DoctorRequestDTO request = DoctorRequestDTO.builder()
                .firstName("Carl")
                .lastName("Edge")
                .gender(Genders.MALE)
                .email("test@mail.com")
                .phoneNumber("+1 (440) 330 33219")
                .specialization("Gynaecologist")
                .rating(BigDecimal.valueOf(0.0))
                .doctorStatus(DoctorStatus.ACTIVE)
                .schedule(List.of(firstScheduleRequest, secondScheduleRequest))
                .build();

        Doctor doc = Doctor.builder()
                .doctorId(UUID.randomUUID())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .specialization(request.getSpecialization())
                .rating(request.getRating())
                .doctorStatus(request.getDoctorStatus())
                .schedules(DoctorMapper.toModelSchedule(request.getSchedule()))
                .build();

        when(doctorRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(doctorRepository.save(Mockito.any(Doctor.class))).thenReturn(doc);

        DoctorResponseDTO response = doctorService.createDoctor(request);

        assertThat(response.getId()).isEqualTo(doc.getDoctorId().toString());
        assertThat(response.getFirstName()).isEqualTo(doc.getFirstName());
        assertThat(response.getLastName()).isEqualTo(doc.getLastName());
        assertThat(response.getGender()).isEqualTo(doc.getGender().toString());
        assertThat(response.getEmail()).isEqualTo(doc.getEmail());
        assertThat(response.getPhoneNumber()).isEqualTo(doc.getPhoneNumber());
        assertThat(response.getSpecialization()).isEqualTo(doc.getSpecialization());
        assertThat(response.getRating()).isEqualTo(doc.getRating().toString());
        assertThat(response.getDoctorStatus()).isEqualTo(doc.getDoctorStatus().toString());
        assertThat(response.getScheduleList().size()).isEqualTo(2);
        assertThat(response.getScheduleList().get(0).getDayOfWeek()).isEqualTo(doc.getSchedules().get(0).getCustomDayOfTheWeek().toString());
        assertThat(response.getScheduleList().get(1).getDayOfWeek()).isEqualTo(doc.getSchedules().get(1).getCustomDayOfTheWeek().toString());

        verify(doctorRepository).existsByEmail(request.getEmail());
        verify(doctorRepository).save(Mockito.any(Doctor.class));
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void saveDoctorShouldNotSaveDoctorIfEmailIsExists() {
        ScheduleRequestDTO firstScheduleRequest = scheduleRequestBuilder();

        ScheduleRequestDTO secondScheduleRequest = scheduleRequestBuilder();
        secondScheduleRequest.setCustomDayOfTheWeek(CustomDayOfTheWeek.TUESDAY);

        DoctorRequestDTO request = DoctorRequestDTO.builder()
                .firstName("Carl")
                .lastName("Edge")
                .gender(Genders.MALE)
                .email("test@mail.com")
                .phoneNumber("+1 (440) 330 33219")
                .specialization("Gynaecologist")
                .rating(BigDecimal.valueOf(0.0))
                .doctorStatus(DoctorStatus.ACTIVE)
                .schedule(List.of(firstScheduleRequest, secondScheduleRequest))
                .build();

        when(doctorRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> doctorService.createDoctor(request));

        verify(doctorRepository).existsByEmail(request.getEmail());
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void updateDoctorShouldUpdateDoctorFromRequest() {
        ScheduleRequestDTO firstScheduleRequest = scheduleRequestBuilder();

        ScheduleRequestDTO secondScheduleRequest = scheduleRequestBuilder();
        secondScheduleRequest.setCustomDayOfTheWeek(CustomDayOfTheWeek.TUESDAY);

        Doctor savedDoc = new Doctor();
        savedDoc.setDoctorId(UUID.randomUUID());
        savedDoc.setSchedules(new ArrayList<>());

        when(doctorRepository.findById(Mockito.any())).thenReturn(Optional.of(savedDoc));

        DoctorRequestDTO request = DoctorRequestDTO.builder()
                .firstName("Carl")
                .lastName("Edge")
                .gender(Genders.MALE)
                .email("test@mail.com")
                .phoneNumber("+1 (440) 330 33219")
                .specialization("Gynaecologist")
                .rating(BigDecimal.valueOf(0.0))
                .doctorStatus(DoctorStatus.ACTIVE)
                .schedule(List.of(firstScheduleRequest, secondScheduleRequest))
                .build();

        UUID doctorId = UUID.randomUUID();

        when(doctorRepository.existsByEmailAndDoctorIdNot(request.getEmail(), doctorId)).thenReturn(false);

        Doctor doctor = Doctor.builder()
                .doctorId(doctorId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .specialization(request.getSpecialization())
                .rating(request.getRating())
                .doctorStatus(request.getDoctorStatus())
                .schedules(DoctorMapper.toModelSchedule(request.getSchedule()))
                .build();

        when(doctorRepository.save(Mockito.any(Doctor.class))).thenReturn(doctor);

        DoctorResponseDTO response = doctorService.updateDoctor(doctorId, request);

        assertThat(response.getId()).isEqualTo(doctorId.toString());
        assertThat(response.getFirstName()).isEqualTo(doctor.getFirstName());
        assertThat(response.getLastName()).isEqualTo(doctor.getLastName());
        assertThat(response.getGender()).isEqualTo(doctor.getGender().toString());
        assertThat(response.getEmail()).isEqualTo(doctor.getEmail());
        assertThat(response.getPhoneNumber()).isEqualTo(doctor.getPhoneNumber());
        assertThat(response.getSpecialization()).isEqualTo(doctor.getSpecialization());
        assertThat(response.getRating()).isEqualTo(doctor.getRating().toString());
        assertThat(response.getDoctorStatus()).isEqualTo(doctor.getDoctorStatus().toString());
        assertThat(response.getScheduleList().size()).isEqualTo(2);
        assertThat(response.getScheduleList().get(0).getDayOfWeek()).isEqualTo(doctor.getSchedules().get(0).getCustomDayOfTheWeek().toString());
        assertThat(response.getScheduleList().get(1).getDayOfWeek()).isEqualTo(doctor.getSchedules().get(1).getCustomDayOfTheWeek().toString());

        verify(doctorRepository).findById(doctorId);
        verify(doctorRepository).existsByEmailAndDoctorIdNot(request.getEmail(), doctorId);
        verify(doctorRepository).save(Mockito.any(Doctor.class));
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void updateDoctorShouldNotUpdateDoctorIfRequestValueIsEmpty() {
        when(doctorRepository.findById(Mockito.any())).thenReturn(Optional.empty());

        assertThrows(DoctorNotFoundException.class, () -> doctorService.updateDoctor(UUID.randomUUID(), DoctorRequestDTO.builder().build()));

        verify(doctorRepository).findById(Mockito.any());
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void updateDoctorShouldNotUpdateDoctorIfEmailIsAlreadyExists() {
        ScheduleRequestDTO firstScheduleRequest = scheduleRequestBuilder();

        ScheduleRequestDTO secondScheduleRequest = scheduleRequestBuilder();
        secondScheduleRequest.setCustomDayOfTheWeek(CustomDayOfTheWeek.TUESDAY);

        Doctor savedDoc = new Doctor();
        savedDoc.setDoctorId(UUID.randomUUID());
        savedDoc.setSchedules(new ArrayList<>());

        when(doctorRepository.findById(Mockito.any())).thenReturn(Optional.of(savedDoc));

        DoctorRequestDTO request = DoctorRequestDTO.builder()
                .firstName("Carl")
                .lastName("Edge")
                .gender(Genders.MALE)
                .email("test@mail.com")
                .phoneNumber("+1 (440) 330 33219")
                .specialization("Gynaecologist")
                .rating(BigDecimal.valueOf(0.0))
                .doctorStatus(DoctorStatus.ACTIVE)
                .schedule(List.of(firstScheduleRequest, secondScheduleRequest))
                .build();

        Doctor doc = Doctor.builder()
                .doctorId(UUID.randomUUID())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .specialization(request.getSpecialization())
                .rating(request.getRating())
                .doctorStatus(request.getDoctorStatus())
                .schedules(DoctorMapper.toModelSchedule(request.getSchedule()))
                .build();

        when(doctorRepository.existsByEmailAndDoctorIdNot(doc.getEmail(), doc.getDoctorId())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> doctorService.updateDoctor(doc.getDoctorId(), request));

        verify(doctorRepository).findById(doc.getDoctorId());
        verify(doctorRepository).existsByEmailAndDoctorIdNot(request.getEmail(), doc.getDoctorId());
        verifyNoMoreInteractions(doctorRepository);
    }

    @Test
    void deleteDoctorShouldDeleteDoctor() {
        UUID id = UUID.randomUUID();

        doctorRepository.deleteById(id);

        verify(doctorRepository).deleteById(id);
        verifyNoMoreInteractions(doctorRepository);
    }

    //Helper method to build schedule models inside doctor model
    private Schedule buildSchedule() {
        return Schedule.builder()
                .scheduleId(UUID.randomUUID())
                .startTime(LocalTime.of(9,0))
                .endTime(LocalTime.of(16, 0))
                .scheduleDate(LocalDate.of(2025, 11, 20))
                .breakStartTime(LocalTime.of(13, 0))
                .breakEndTime(LocalTime.of(14, 0))
                .customDayOfTheWeek(CustomDayOfTheWeek.MONDAY)
                .isDayOff(false)
                .build();
    }

    //Helper method to build schedule request inside doctor model
    private ScheduleRequestDTO scheduleRequestBuilder (){
        return ScheduleRequestDTO.builder()
                .scheduleStartTime(LocalTime.of(9,0))
                .scheduleEndTime(LocalTime.of(16, 0))
                .scheduleDate(LocalDate.of(2025, 11, 20))
                .breakStartTime(LocalTime.of(13, 0))
                .breakEndTime(LocalTime.of(14, 0))
                .customDayOfTheWeek(CustomDayOfTheWeek.TUESDAY)
                .isDayOff(false)
                .build();
    }
}