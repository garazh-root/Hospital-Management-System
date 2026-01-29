package org.com.doctorservice.mapper;

import org.com.doctorservice.additional.CustomDayOfTheWeek;
import org.com.doctorservice.additional.DoctorStatus;
import org.com.doctorservice.dto.DoctorRequestDTO;
import org.com.doctorservice.dto.DoctorResponseDTO;
import org.com.doctorservice.exception.EmptyScheduleException;
import org.com.doctorservice.model.Doctor;
import org.com.doctorservice.additional.Genders;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DoctorMapperTest {

    @Test
    void toResponseDTOShouldReturnDoctorResponseDTO() {
        Schedule schedule = buildSchedule();

        Doctor doctor = Doctor.builder()
                .doctorId(UUID.randomUUID())
                .firstName("Jessy")
                .lastName("Mohawk")
                .gender(Genders.MALE)
                .email("test@mail.com")
                .phoneNumber("+111111111")
                .specialization("Obstetrician")
                .rating(BigDecimal.valueOf(7.3))
                .doctorStatus(DoctorStatus.ACTIVE)
                .schedules(List.of(schedule))
                .build();

        DoctorResponseDTO doctorResponseDTO = DoctorMapper.toResponseDTO(doctor);

        assertThat(doctorResponseDTO.getId()).isEqualTo(doctor.getDoctorId().toString());
        assertThat(doctorResponseDTO.getFirstName()).isEqualTo(doctor.getFirstName());
        assertThat(doctorResponseDTO.getLastName()).isEqualTo(doctor.getLastName());
        assertThat(doctorResponseDTO.getEmail()).isEqualTo(doctor.getEmail());
        assertThat(doctorResponseDTO.getPhoneNumber()).isEqualTo(doctor.getPhoneNumber());
        assertThat(doctorResponseDTO.getSpecialization()).isEqualTo(doctor.getSpecialization());
        assertThat(doctorResponseDTO.getRating()).isEqualTo(doctor.getRating().toString());
        assertThat(doctorResponseDTO.getDoctorStatus()).isEqualTo(doctor.getDoctorStatus().toString());
        assertThat(doctorResponseDTO.getScheduleList().size()).isEqualTo(1);
        assertThat(doctorResponseDTO.getScheduleList().get(0).getDayOfWeek()).isEqualTo("MONDAY");
    }

    @Test
    void toResponseShouldThrowEmptyModelExceptionWhenDoctorIsNull() {
        Doctor doctor = null;

        Assertions.assertThrows(NullPointerException.class, () -> DoctorMapper.toResponseDTO(doctor));
    }

    @Test
    void toModelShouldReturnEntity () {
        ScheduleRequestDTO scheduleRequest = scheduleRequestBuilder();

        DoctorRequestDTO request = DoctorRequestDTO.builder()
                .firstName("Bob")
                .lastName("Incredible")
                .gender(Genders.MALE)
                .email("test@mail.com")
                .phoneNumber("+1 (440) 22 320 9006")
                .specialization("Dentist")
                .rating(BigDecimal.valueOf(0.0))
                .doctorStatus(DoctorStatus.ACTIVE)
                .schedule(List.of(scheduleRequest))
                .build();

        Doctor doc = DoctorMapper.toModel(request);

        assertThat(doc.getFirstName()).isEqualTo(request.getFirstName());
        assertThat(doc.getLastName()).isEqualTo(request.getLastName());
        assertThat(doc.getGender()).isEqualTo(request.getGender());
        assertThat(doc.getEmail()).isEqualTo(request.getEmail());
        assertThat(doc.getPhoneNumber()).isEqualTo(request.getPhoneNumber());
        assertThat(doc.getSpecialization()).isEqualTo(request.getSpecialization());
        assertThat(doc.getRating()).isEqualTo(request.getRating());
        assertThat(doc.getDoctorStatus()).isEqualTo(request.getDoctorStatus());
        assertThat(doc.getSchedules().size()).isEqualTo(request.getSchedule().size());
        assertThat(doc.getSchedules().get(0).getCustomDayOfTheWeek()).isEqualTo(CustomDayOfTheWeek.TUESDAY);
        assertThat(doc.getSchedules().get(0).getScheduleDate()).isEqualTo(request.getSchedule().get(0).getScheduleDate());
    }

    @Test
    void toModelShouldThrowEmptyComponentExceptionWhenRequestIsNull () {
        DoctorRequestDTO request = null;

        Assertions.assertThrows(NullPointerException.class, () -> DoctorMapper.toModel(request));
    }

    @Test
    void toScheduleDTOShouldReturnListOfScheduleResponse () {
        Schedule firstDoctorSchedule = Schedule.builder()
                .scheduleId(UUID.randomUUID())
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(16, 0))
                .scheduleDate(LocalDate.of(2025, 11, 21))
                .breakStartTime(LocalTime.of(13, 0))
                .breakEndTime(LocalTime.of(13, 30))
                .customDayOfTheWeek(CustomDayOfTheWeek.TUESDAY)
                .isDayOff(false)
                .build();

        Schedule secondDoctorSchedule = Schedule.builder()
                .scheduleId(UUID.randomUUID())
                .startTime(LocalTime.of(10, 0))
                .endTime(LocalTime.of(16, 30))
                .scheduleDate(LocalDate.of(2025, 11, 23))
                .breakStartTime(LocalTime.of(14, 0))
                .breakEndTime(LocalTime.of(15, 0))
                .customDayOfTheWeek(CustomDayOfTheWeek.THURSDAY)
                .isDayOff(false)
                .build();

        List<ScheduleResponseDTO> listOfSchedules = DoctorMapper.toScheduleDTO(List.of(firstDoctorSchedule, secondDoctorSchedule));

        assertThat(listOfSchedules.size()).isEqualTo(2);

        ScheduleResponseDTO firstSchedule = listOfSchedules.get(0);
        ScheduleResponseDTO secondSchedule = listOfSchedules.get(1);

        assertThat(firstSchedule.getScheduleStartTime()).isEqualTo(firstDoctorSchedule.getStartTime().toString());
        assertThat(firstSchedule.getScheduleEndTime()).isEqualTo(firstDoctorSchedule.getEndTime().toString());
        assertThat(firstSchedule.getScheduleDate()).isEqualTo(firstDoctorSchedule.getScheduleDate().toString());
        assertThat(firstSchedule.getBreakStartTime()).isEqualTo(firstDoctorSchedule.getBreakStartTime().toString());
        assertThat(firstSchedule.getBreakEndTime()).isEqualTo(firstDoctorSchedule.getBreakEndTime().toString());

        assertThat(secondSchedule.getScheduleStartTime()).isEqualTo(secondDoctorSchedule.getStartTime().toString());
        assertThat(secondSchedule.getScheduleEndTime()).isEqualTo(secondDoctorSchedule.getEndTime().toString());
        assertThat(secondSchedule.getScheduleDate()).isEqualTo(secondDoctorSchedule.getScheduleDate().toString());
        assertThat(secondSchedule.getBreakStartTime()).isEqualTo(secondDoctorSchedule.getBreakStartTime().toString());
        assertThat(secondSchedule.getBreakEndTime()).isEqualTo(secondDoctorSchedule.getBreakEndTime().toString());
    }

    @Test
    void toScheduleDTOShouldThrowEmptyScheduleExceptionWhenRequestIsEmpty () {
        Assertions.assertThrows(EmptyScheduleException.class, () -> DoctorMapper.toScheduleDTO(null));
    }

    @Test
    void toModelScheduleShouldReturnListOfSchedules () {
        ScheduleRequestDTO firstScheduleRequest = ScheduleRequestDTO.builder()
                .scheduleStartTime(LocalTime.of(9, 0))
                .scheduleEndTime(LocalTime.of(16, 30))
                .scheduleDate(LocalDate.of(2025, 11, 20))
                .breakStartTime(LocalTime.of(13, 0))
                .breakEndTime(LocalTime.of(14, 0))
                .customDayOfTheWeek(CustomDayOfTheWeek.MONDAY)
                .isDayOff(false)
                .build();

        ScheduleRequestDTO secondScheduleRequest = ScheduleRequestDTO.builder()
                .scheduleStartTime(LocalTime.of(11, 0))
                .scheduleEndTime(LocalTime.of(17, 30))
                .scheduleDate(LocalDate.of(2025, 11, 22))
                .breakStartTime(LocalTime.of(14, 30))
                .breakEndTime(LocalTime.of(15, 30))
                .customDayOfTheWeek(CustomDayOfTheWeek.WEDNESDAY)
                .isDayOff(false)
                .build();

        List<Schedule> listOfSchedules = DoctorMapper.toModelSchedule(List.of(firstScheduleRequest, secondScheduleRequest));

        assertThat(listOfSchedules.size()).isEqualTo(2);

        Schedule firstSchedule = listOfSchedules.get(0);
        Schedule secondSchedule = listOfSchedules.get(1);

        assertThat(firstScheduleRequest.getScheduleStartTime()).isEqualTo(firstSchedule.getStartTime().toString());
        assertThat(firstScheduleRequest.getScheduleEndTime()).isEqualTo(firstSchedule.getEndTime().toString());
        assertThat(firstScheduleRequest.getScheduleDate()).isEqualTo(firstSchedule.getScheduleDate().toString());
        assertThat(firstScheduleRequest.getBreakStartTime()).isEqualTo(firstSchedule.getBreakStartTime().toString());
        assertThat(firstScheduleRequest.getBreakEndTime()).isEqualTo(firstSchedule.getBreakEndTime().toString());

        assertThat(secondScheduleRequest.getScheduleStartTime()).isEqualTo(secondSchedule.getStartTime().toString());
        assertThat(secondScheduleRequest.getScheduleEndTime()).isEqualTo(secondSchedule.getEndTime().toString());
        assertThat(secondScheduleRequest.getScheduleDate()).isEqualTo(secondSchedule.getScheduleDate().toString());
        assertThat(secondScheduleRequest.getBreakStartTime()).isEqualTo(secondSchedule.getBreakStartTime().toString());
        assertThat(secondScheduleRequest.getBreakEndTime()).isEqualTo(secondSchedule.getBreakEndTime().toString());
    }

    @Test
    void toScheduleModelShouldThrowEmptyScheduleExceptionWhenRequestIsEmpty () {
        Assertions.assertThrows(EmptyScheduleException.class, () -> DoctorMapper.toModelSchedule(null));
    }

    @Test
    void shouldThrowEmptyScheduleExceptionWhenListOfSchedulesIsEmpty () {
        Assertions.assertThrows(EmptyScheduleException.class, () -> DoctorMapper.toModelSchedule(List.of()));
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