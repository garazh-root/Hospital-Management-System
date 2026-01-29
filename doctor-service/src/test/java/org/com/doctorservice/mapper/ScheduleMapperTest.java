package org.com.doctorservice.mapper;
//
//import org.com.doctorservice.additional.CustomDayOfTheWeek;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.UUID;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
public class ScheduleMapperTest {

}
//
//    @Test
//    void mapListOfSchedulesToResponseDTOShouldReturnListOfSchedules() {
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
//        List<ScheduleResponseDTO> schedulesResponseDTOsList = ScheduleMapper.mapListOfSchedulesToResponseDTO(List.of(firstSchedule, secondSchedule));
//
//        assertThat(schedulesResponseDTOsList.size()).isEqualTo(2);
//
//        assertThat(schedulesResponseDTOsList.get(0).getScheduleStartTime()).isEqualTo(firstSchedule.getStartTime().toString());
//        assertThat(schedulesResponseDTOsList.get(0).getScheduleEndTime()).isEqualTo(firstSchedule.getEndTime().toString());
//
//        assertThat(schedulesResponseDTOsList.get(1).getScheduleStartTime()).isEqualTo(secondSchedule.getStartTime().toString());
//        assertThat(schedulesResponseDTOsList.get(1).getScheduleEndTime()).isEqualTo(secondSchedule.getEndTime().toString());
//    }
//
//    @Test
//    void mapScheduleToResponseDTOShouldReturnScheduleResponseDTO() {
//
//        Schedule schedule = Schedule.builder()
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
//        ScheduleResponseDTO scheduleResponseDTO = ScheduleResponseDTO.builder()
//                .scheduleStartTime(schedule.getStartTime().toString())
//                .scheduleEndTime(schedule.getEndTime().toString())
//                .scheduleDate(schedule.getScheduleDate().toString())
//                .breakStartTime(schedule.getBreakStartTime().toString())
//                .breakEndTime(schedule.getBreakEndTime().toString())
//                .dayOfWeek(schedule.getCustomDayOfTheWeek().toString())
//                .isDayOff(String.valueOf(schedule.isDayOff()))
//                .build();
//
//        assertThat(scheduleResponseDTO.getScheduleStartTime()).isEqualTo(schedule.getStartTime().toString());
//        assertThat(scheduleResponseDTO.getScheduleEndTime()).isEqualTo(schedule.getEndTime().toString());
//        assertThat(scheduleResponseDTO.getScheduleDate()).isEqualTo(schedule.getScheduleDate().toString());
//    }
//}