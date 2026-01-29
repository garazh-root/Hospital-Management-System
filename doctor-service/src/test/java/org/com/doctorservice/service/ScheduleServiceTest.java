package org.com.doctorservice.service;
//
//import org.com.doctorservice.additional.CustomDayOfTheWeek;
//import org.com.doctorservice.exception.EmptyScheduleException;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
//import static org.mockito.Mockito.when;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(SpringExtension.class)
public class ScheduleServiceTest {
}
//
//    @InjectMocks
//    private ScheduleService scheduleService;
//
//    @Mock
//    private ScheduleRepository scheduleRepository;
//
//    @Test
//    void findSchedulesByIdShouldReturnListOfSchedules() {
//        UUID doctorId = UUID.randomUUID();
//
//        Schedule firstSchedule = Schedule.builder()
//                .scheduleId(UUID.randomUUID())
//                .startTime(LocalTime.of(9, 0))
//                .endTime(LocalTime.of(16, 0))
//                .scheduleDate(LocalDate.of(2025, 11, 25))
//                .breakStartTime(LocalTime.of(13, 0))
//                .breakEndTime(LocalTime.of(14, 0))
//                .customDayOfTheWeek(CustomDayOfTheWeek.MONDAY)
//                .isDayOff(false)
//                .build();
//
//        Schedule secondSchedule = Schedule.builder()
//                .scheduleId(UUID.randomUUID())
//                .startTime(LocalTime.of(10, 0))
//                .endTime(LocalTime.of(16, 0))
//                .scheduleDate(LocalDate.of(2025, 11, 27))
//                .breakStartTime(LocalTime.of(13, 0))
//                .breakEndTime(LocalTime.of(14, 0))
//                .customDayOfTheWeek(CustomDayOfTheWeek.WEDNESDAY)
//                .isDayOff(false)
//                .build();
//
//        when(scheduleRepository.findByDoctorDoctorId(doctorId)).thenReturn(List.of(firstSchedule, secondSchedule));
//
//        List<ScheduleResponseDTO> scheduleDTOsList = scheduleService.findAllByDoctorId(doctorId);
//
//        assertThat(scheduleDTOsList.size()).isEqualTo(2);
//
//        assertThat(scheduleDTOsList.get(0).getScheduleStartTime()).isEqualTo(firstSchedule.getStartTime().toString());
//        assertThat(scheduleDTOsList.get(0).getScheduleEndTime()).isEqualTo(firstSchedule.getEndTime().toString());
//        assertThat(scheduleDTOsList.get(0).getScheduleDate()).isEqualTo(firstSchedule.getScheduleDate().toString());
//        assertThat(scheduleDTOsList.get(0).getBreakStartTime()).isEqualTo(firstSchedule.getBreakStartTime().toString());
//        assertThat(scheduleDTOsList.get(0).getBreakEndTime()).isEqualTo(firstSchedule.getBreakEndTime().toString());
//        assertThat(scheduleDTOsList.get(0).getDayOfWeek()).isEqualTo(firstSchedule.getCustomDayOfTheWeek().toString());
//
//        assertThat(scheduleDTOsList.get(1).getScheduleStartTime()).isEqualTo(secondSchedule.getStartTime().toString());
//        assertThat(scheduleDTOsList.get(1).getScheduleEndTime()).isEqualTo(secondSchedule.getEndTime().toString());
//        assertThat(scheduleDTOsList.get(1).getScheduleDate()).isEqualTo(secondSchedule.getScheduleDate().toString());
//        assertThat(scheduleDTOsList.get(1).getBreakStartTime()).isEqualTo(secondSchedule.getBreakStartTime().toString());
//        assertThat(scheduleDTOsList.get(1).getBreakEndTime()).isEqualTo(secondSchedule.getBreakEndTime().toString());
//        assertThat(scheduleDTOsList.get(1).getDayOfWeek()).isEqualTo(secondSchedule.getCustomDayOfTheWeek().toString());
//
//        verify(scheduleRepository).findByDoctorDoctorId(doctorId);
//        verifyNoMoreInteractions(scheduleRepository);
//    }
//
//    @Test
//    void findSchedulesByIdShouldReturnEmptyListWhenNoSchedulesFound () {
//        UUID doctorId = UUID.randomUUID();
//
//        when(scheduleRepository.findByDoctorDoctorId(doctorId)).thenReturn(List.of());
//
//        List<ScheduleResponseDTO> result = scheduleService.findAllByDoctorId(doctorId);
//
//        assertThat(result.isEmpty()).isTrue();
//
//        verify(scheduleRepository).findByDoctorDoctorId(doctorId);
//        verifyNoMoreInteractions(scheduleRepository);
//    }
//
//    @Test
//    void findSchedulesByIdAndDayOfTheWeekShouldReturnListOfSchedules() {
//        UUID doctorId = UUID.randomUUID();
//
//        CustomDayOfTheWeek day = CustomDayOfTheWeek.MONDAY;
//
//        Schedule firstSchedule = Schedule.builder()
//                .scheduleId(UUID.randomUUID())
//                .startTime(LocalTime.of(9, 0))
//                .endTime(LocalTime.of(16, 0))
//                .scheduleDate(LocalDate.of(2025, 11, 25))
//                .breakStartTime(LocalTime.of(13, 0))
//                .breakEndTime(LocalTime.of(14, 0))
//                .customDayOfTheWeek(CustomDayOfTheWeek.MONDAY)
//                .isDayOff(false)
//                .build();
//
//        Schedule secondSchedule = Schedule.builder()
//                .scheduleId(UUID.randomUUID())
//                .startTime(LocalTime.of(10, 0))
//                .endTime(LocalTime.of(16, 0))
//                .scheduleDate(LocalDate.of(2025, 11, 15))
//                .breakStartTime(LocalTime.of(13, 0))
//                .breakEndTime(LocalTime.of(14, 0))
//                .customDayOfTheWeek(CustomDayOfTheWeek.MONDAY)
//                .isDayOff(false)
//                .build();
//
//        when(scheduleRepository.findByDoctorDoctorIdAndCustomDayOfTheWeek(doctorId, day)).thenReturn(List.of(firstSchedule, secondSchedule));
//
//        List<ScheduleResponseDTO> listOfSchedulesDTOs = scheduleService.findAllByDoctorIdAndCustomDayOfTheWeek(doctorId, day);
//
//        assertThat(listOfSchedulesDTOs.size()).isEqualTo(2);
//
//        assertThat(listOfSchedulesDTOs.get(0).getScheduleStartTime()).isEqualTo(firstSchedule.getStartTime().toString());
//        assertThat(listOfSchedulesDTOs.get(0).getScheduleEndTime()).isEqualTo(firstSchedule.getEndTime().toString());
//        assertThat(listOfSchedulesDTOs.get(0).getScheduleDate()).isEqualTo(firstSchedule.getScheduleDate().toString());
//        assertThat(listOfSchedulesDTOs.get(0).getDayOfWeek()).isEqualTo(firstSchedule.getCustomDayOfTheWeek().toString());
//
//        assertThat(listOfSchedulesDTOs.get(1).getScheduleStartTime()).isEqualTo(secondSchedule.getStartTime().toString());
//        assertThat(listOfSchedulesDTOs.get(1).getScheduleEndTime()).isEqualTo(secondSchedule.getEndTime().toString());
//        assertThat(listOfSchedulesDTOs.get(1).getScheduleDate()).isEqualTo(secondSchedule.getScheduleDate().toString());
//        assertThat(listOfSchedulesDTOs.get(1).getDayOfWeek()).isEqualTo(secondSchedule.getCustomDayOfTheWeek().toString());
//
//        verify(scheduleRepository).findByDoctorDoctorIdAndCustomDayOfTheWeek(doctorId, day);
//        verifyNoMoreInteractions(scheduleRepository);
//    }
//
//    @Test
//    void findSchedulesByIdAndDayOfTheWeemShouldReturnEmptyListWhenNoSchedulesFound () {
//        UUID doctorId = UUID.randomUUID();
//
//        when(scheduleRepository.findByDoctorDoctorId(doctorId)).thenReturn(List.of());
//
//        List<ScheduleResponseDTO> result = scheduleService.findAllByDoctorId(doctorId);
//
//        assertThat(result.isEmpty()).isTrue();
//
//        verify(scheduleRepository).findByDoctorDoctorId(doctorId);
//        verifyNoMoreInteractions(scheduleRepository);
//    }
//
//    @Test
//    void findSchedulesByIdAndScheduleDateBetweenShouldReturnListOfSchedules() {
//        UUID doctorId = UUID.randomUUID();
//
//        LocalDate firstScheduleDate = LocalDate.of(2025, 11, 10);
//        LocalDate secondScheduleDate = LocalDate.of(2025, 11, 30);
//
//        Schedule firstSchedule = Schedule.builder()
//                .scheduleId(UUID.randomUUID())
//                .startTime(LocalTime.of(9, 0))
//                .endTime(LocalTime.of(16, 0))
//                .scheduleDate(LocalDate.of(2025, 11, 15))
//                .breakStartTime(LocalTime.of(13, 0))
//                .breakEndTime(LocalTime.of(14, 0))
//                .customDayOfTheWeek(CustomDayOfTheWeek.MONDAY)
//                .isDayOff(false)
//                .build();
//
//        Schedule secondSchedule = Schedule.builder()
//                .scheduleId(UUID.randomUUID())
//                .startTime(LocalTime.of(10, 0))
//                .endTime(LocalTime.of(16, 0))
//                .scheduleDate(LocalDate.of(2025, 11, 20))
//                .breakStartTime(LocalTime.of(13, 0))
//                .breakEndTime(LocalTime.of(14, 0))
//                .customDayOfTheWeek(CustomDayOfTheWeek.FRIDAY)
//                .isDayOff(false)
//                .build();
//
//        when(scheduleRepository.findByDoctorDoctorIdAndScheduleDateBetween(doctorId, firstScheduleDate, secondScheduleDate)).
//                thenReturn(List.of(firstSchedule, secondSchedule));
//
//        List<ScheduleResponseDTO> listOfSchedulesDTOs = scheduleService.findAllByDoctorIdAndScheduleDateBetween(doctorId, firstScheduleDate, secondScheduleDate);
//
//        assertThat(listOfSchedulesDTOs.size()).isEqualTo(2);
//
//        assertThat(listOfSchedulesDTOs.get(0).getScheduleStartTime()).isEqualTo(firstSchedule.getStartTime().toString());
//        assertThat(listOfSchedulesDTOs.get(0).getScheduleEndTime()).isEqualTo(firstSchedule.getEndTime().toString());
//        assertThat(listOfSchedulesDTOs.get(0).getScheduleDate()).isEqualTo(firstSchedule.getScheduleDate().toString());
//
//        assertThat(listOfSchedulesDTOs.get(1).getScheduleStartTime()).isEqualTo(secondSchedule.getStartTime().toString());
//        assertThat(listOfSchedulesDTOs.get(1).getScheduleEndTime()).isEqualTo(secondSchedule.getEndTime().toString());
//        assertThat(listOfSchedulesDTOs.get(1).getScheduleDate()).isEqualTo(secondSchedule.getScheduleDate().toString());
//
//        verify(scheduleRepository).findByDoctorDoctorIdAndScheduleDateBetween(doctorId, firstScheduleDate, secondScheduleDate);
//        verifyNoMoreInteractions(scheduleRepository);
//    }
//
//    @Test
//    void findSchedulesByIdAndScheduleDateBetweenShouldReturnEmptyListWhenNoSchedulesFound () {
//        UUID doctorId = UUID.randomUUID();
//
//        LocalDate firstScheduleDate = LocalDate.of(2025, 11, 10);
//        LocalDate secondScheduleDate = LocalDate.of(2025, 11, 30);
//
//        when(scheduleRepository.findByDoctorDoctorIdAndScheduleDateBetween(doctorId, firstScheduleDate, secondScheduleDate)).thenReturn(List.of());
//
//        List<ScheduleResponseDTO> result = scheduleService.findAllByDoctorIdAndScheduleDateBetween(doctorId, firstScheduleDate, secondScheduleDate);
//
//        assertThat(result.isEmpty()).isTrue();
//
//        verify(scheduleRepository).findByDoctorDoctorIdAndScheduleDateBetween(doctorId, firstScheduleDate, secondScheduleDate);
//        verifyNoMoreInteractions(scheduleRepository);
//    }
//
//    @Test
//    void findSchedulesByIdAndStartTimeBetweenAndEndTimeShouldReturnListOfSchedules () {
//        UUID doctorId = UUID.randomUUID();
//
//        LocalTime startTime = LocalTime.of(9, 0);
//        LocalTime endTime = LocalTime.of(16, 0);
//
//        Schedule firstSchedule = Schedule.builder()
//                .scheduleId(UUID.randomUUID())
//                .startTime(LocalTime.of(10, 0))
//                .endTime(LocalTime.of(16, 0))
//                .scheduleDate(LocalDate.of(2025, 11, 15))
//                .breakStartTime(LocalTime.of(13, 0))
//                .breakEndTime(LocalTime.of(14, 0))
//                .customDayOfTheWeek(CustomDayOfTheWeek.MONDAY)
//                .isDayOff(false)
//                .build();
//
//        Schedule secondSchedule = Schedule.builder()
//                .scheduleId(UUID.randomUUID())
//                .startTime(LocalTime.of(10, 0))
//                .endTime(LocalTime.of(16, 0))
//                .scheduleDate(LocalDate.of(2025, 11, 20))
//                .breakStartTime(LocalTime.of(13, 0))
//                .breakEndTime(LocalTime.of(14, 0))
//                .customDayOfTheWeek(CustomDayOfTheWeek.FRIDAY)
//                .isDayOff(false)
//                .build();
//
//        when(scheduleRepository.findByDoctorDoctorIdAndStartTimeBeforeAndEndTimeAfter(doctorId, startTime, endTime)).thenReturn(List.of(firstSchedule, secondSchedule));
//
//        List<ScheduleResponseDTO> listOfSchedulesDTOs = scheduleService.findAllByDoctorIdAndStartTimeBetweenAndEndTimeBetween(
//                doctorId, startTime, endTime
//        );
//
//        assertThat(listOfSchedulesDTOs.size()).isEqualTo(2);
//
//        assertThat(listOfSchedulesDTOs.get(0).getScheduleStartTime()).isEqualTo(firstSchedule.getStartTime().toString());
//        assertThat(listOfSchedulesDTOs.get(0).getScheduleEndTime()).isEqualTo(firstSchedule.getEndTime().toString());
//        assertThat(listOfSchedulesDTOs.get(0).getScheduleDate()).isEqualTo(firstSchedule.getScheduleDate().toString());
//
//        assertThat(listOfSchedulesDTOs.get(1).getScheduleStartTime()).isEqualTo(secondSchedule.getStartTime().toString());
//        assertThat(listOfSchedulesDTOs.get(1).getScheduleEndTime()).isEqualTo(secondSchedule.getEndTime().toString());
//        assertThat(listOfSchedulesDTOs.get(1).getScheduleDate()).isEqualTo(secondSchedule.getScheduleDate().toString());
//
//        verify(scheduleRepository).findByDoctorDoctorIdAndStartTimeBeforeAndEndTimeAfter(
//                doctorId, startTime, endTime
//        );
//        verifyNoMoreInteractions(scheduleRepository);
//    }
//
//    @Test
//    void findScheduleByIdAndStartTimeBetweenAndEndTimeShouldReturnEmptyListWhenNoSchedulesFound () {
//        UUID doctorId = UUID.randomUUID();
//
//        LocalTime startTime = LocalTime.of(10, 0);
//        LocalTime endTime = LocalTime.of(16, 0);
//
//        when(scheduleRepository.findByDoctorDoctorIdAndStartTimeBeforeAndEndTimeAfter(
//                doctorId, startTime, endTime)).thenReturn(List.of());
//
//        List<ScheduleResponseDTO> result = scheduleService.findAllByDoctorIdAndStartTimeBetweenAndEndTimeBetween(
//                doctorId, startTime, endTime
//        );
//
//        assertThat(result.isEmpty()).isTrue();
//
//        verify(scheduleRepository).findByDoctorDoctorIdAndStartTimeBeforeAndEndTimeAfter(
//                doctorId, startTime, endTime);
//        verifyNoMoreInteractions(scheduleRepository);
//    }
//
//    @Test
//    void findScheduleByIdAndDateShouldReturnSchedule () {
//        UUID doctorId = UUID.randomUUID();
//
//        LocalDate date = LocalDate.of(2025, 11, 15);
//
//        Schedule firstSchedule = Schedule.builder()
//                .scheduleId(UUID.randomUUID())
//                .startTime(LocalTime.of(10, 0))
//                .endTime(LocalTime.of(16, 0))
//                .scheduleDate(LocalDate.of(2025, 11, 15))
//                .breakStartTime(LocalTime.of(13, 0))
//                .breakEndTime(LocalTime.of(14, 0))
//                .customDayOfTheWeek(CustomDayOfTheWeek.MONDAY)
//                .isDayOff(false)
//                .build();
//
//        when(scheduleRepository.findByDoctorDoctorIdAndScheduleDate(doctorId, date)).thenReturn(Optional.of(firstSchedule));
//
//        ScheduleResponseDTO responseDTO = scheduleService.findByDoctorIdAndScheduleDate(doctorId, date);
//
//        assertThat(responseDTO.getScheduleStartTime()).isEqualTo(firstSchedule.getStartTime().toString());
//        assertThat(responseDTO.getScheduleEndTime()).isEqualTo(firstSchedule.getEndTime().toString());
//        assertThat(responseDTO.getScheduleDate()).isEqualTo(firstSchedule.getScheduleDate().toString());
//
//        verify(scheduleRepository).findByDoctorDoctorIdAndScheduleDate(doctorId, date);
//        verifyNoMoreInteractions(scheduleRepository);
//    }
//
//    @Test
//    void findScheduleByIdAndStartTimeShouldThrowEmptyScheduleExceptionWhenScheduleNotFound () {
//        UUID doctorId = UUID.randomUUID();
//
//        LocalDate date = LocalDate.of(2025, 11, 15);
//
//        when(scheduleRepository.findByDoctorDoctorIdAndScheduleDate(doctorId, date)).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> scheduleService.findByDoctorIdAndScheduleDate(doctorId, date)).isInstanceOf(EmptyScheduleException.class);
//
//        verify(scheduleRepository).findByDoctorDoctorIdAndScheduleDate(doctorId, date);
//        verifyNoMoreInteractions(scheduleRepository);
//    }
//}