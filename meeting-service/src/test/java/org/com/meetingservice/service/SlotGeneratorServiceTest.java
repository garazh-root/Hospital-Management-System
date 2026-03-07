package org.com.meetingservice.service;
//
//import org.com.meetingservice.dto.AvailableSlotResponse;
//import org.com.meetingservice.dto.ScheduleOverrideDTO;
//import org.com.meetingservice.dto.ScheduleResponse;
//import org.com.meetingservice.dto.ScheduleTemplateDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.NoSuchElementException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
public class SlotGeneratorServiceTest {

}
//
//    private SlotGeneratorService slotGeneratorService;
//
//    private static final LocalDate DATE = LocalDate.of(2026, 6, 1);
//
//    @BeforeEach
//    void setUp() {
//        slotGeneratorService = new SlotGeneratorService();
//    }
//
//    @Test
//    void generateSlotsShouldGenerateSlotsAndReturnListOfGeneratedSlotsResponse() {
//        ScheduleTemplateDTO template = templateBuilder(
//                "32daefcc8e0cafd8ceefdad3", "e1dd85d5-29e9-47bb-8124-9d558a5157cc", "MONDAY", "09:00",
//                "10:00", "12:00", "13:00", "30", null, null
//        );
//
//        ScheduleResponse responses = new ScheduleResponse(List.of(template), Collections.emptyList());
//
//        List<AvailableSlotResponse> listOfAvailableSlots = slotGeneratorService.generateAvailableSlots(
//                responses, DATE, Collections.emptyList()
//        );
//
//        assertThat(listOfAvailableSlots).hasSize(2);
//        assertThat(listOfAvailableSlots.getFirst().startTime()).isEqualTo("09:00");
//        assertThat(listOfAvailableSlots.getFirst().endTime()).isEqualTo("09:30");
//        assertThat(listOfAvailableSlots.getLast().startTime()).isEqualTo("09:30");
//        assertThat(listOfAvailableSlots.getLast().endTime()).isEqualTo("10:00");
//    }
//
//    @Test
//    void generateSlotsShouldExcludeBreakTime () {
//        ScheduleTemplateDTO template = templateBuilder(
//                "32daefcc8e0cafd8ceefdad3", "e1dd85d5-29e9-47bb-8124-9d558a5157cc", "MONDAY", "09:00",
//                "14:00", "12:00", "13:00", "30", null, null
//        );
//
//        ScheduleResponse responses = new ScheduleResponse(List.of(template), Collections.emptyList());
//
//        List<AvailableSlotResponse> listOfAvailableSlots = slotGeneratorService.generateAvailableSlots(
//                responses, DATE, Collections.emptyList()
//        );
//
//        List<String> list = listOfAvailableSlots.stream().map(AvailableSlotResponse::startTime).toList();
//
//        assertThat(listOfAvailableSlots).hasSize(8);
//        assertThat(list).doesNotContain("12:00");
//        assertThat(list).doesNotContain("12:30");
//    }
//
//    @Test
//    void generateSlotsShouldExcludeBookedSlots() {
//        ScheduleTemplateDTO template = templateBuilder(
//                "32daefcc8e0cafd8ceefdad3", "e1dd85d5-29e9-47bb-8124-9d558a5157cc", "MONDAY", "09:00",
//                "10:00", "12:00", "13:00", "30", null, null
//        );
//
//        ScheduleResponse responses = new ScheduleResponse(List.of(template), Collections.emptyList());
//
//        List<LocalDateTime> bookedList = List.of(
//                LocalDateTime.of(DATE, LocalTime.of(9, 0))
//        );
//
//        List<AvailableSlotResponse> listOfAvailableSlots = slotGeneratorService.generateAvailableSlots(
//                responses, DATE, bookedList
//        );
//
//        assertThat(listOfAvailableSlots).extracting(AvailableSlotResponse::startTime)
//                .doesNotContain("09:00");
//    }
//
//    @Test
//    void generateSlotsShouldIncludeDateInEachResponse() {
//        ScheduleTemplateDTO template = templateBuilder(
//                "32daefcc8e0cafd8ceefdad3", "e1dd85d5-29e9-47bb-8124-9d558a5157cc", "MONDAY", "09:00",
//                "10:00", "12:00", "13:00", "30", null, null
//        );
//
//        ScheduleResponse responses = new ScheduleResponse(List.of(template), Collections.emptyList());
//
//        List<AvailableSlotResponse> listOfAvailableSlots = slotGeneratorService.generateAvailableSlots(
//                responses, DATE, Collections.emptyList()
//        );
//
//        assertThat(listOfAvailableSlots).allSatisfy(availableSlot -> availableSlot.date().equals(DATE));
//    }
//
//    @Test
//    void generateAvailableSlotsShouldReturnEmptyListIfAllSlotsAreBooked() {
//        ScheduleTemplateDTO template = templateBuilder(
//                "32daefcc8e0cafd8ceefdad3", "e1dd85d5-29e9-47bb-8124-9d558a5157cc", "MONDAY", "09:00",
//                "10:00", "12:00", "13:00", "30", null, null
//        );
//
//        List<LocalDateTime> bookedList = List.of(
//                LocalDateTime.of(DATE, LocalTime.of(9, 0)),
//                LocalDateTime.of(DATE, LocalTime.of(9, 30))
//        );
//
//        ScheduleResponse responses = new ScheduleResponse(List.of(template), Collections.emptyList());
//
//        List<AvailableSlotResponse> listOfAvailableSlots = slotGeneratorService.generateAvailableSlots(
//                responses, DATE, bookedList
//        );
//
//        assertThat(listOfAvailableSlots).isEmpty();
//    }
//
//    @Test
//    void generateAvailableSlotsShouldReturnEmptyListIfOverrideIsEmpty() {
//        ScheduleTemplateDTO template = templateBuilder(
//                "32daefcc8e0cafd8ceefdad3", "e1dd85d5-29e9-47bb-8124-9d558a5157cc", "MONDAY", "09:00",
//                "10:00", "12:00", "13:00", "30", null, null
//        );
//
//        ScheduleOverrideDTO override = new ScheduleOverrideDTO(
//                "beb22788-c5df-485b-9fb0-4484f6eb0d56", "9eba8aae-5a8d-47cb-b21b-f692037e01a5", DATE.toString(),
//                "UNAVAILABLE", null, null, null, null);
//
//        ScheduleResponse responses = new ScheduleResponse(List.of(template), List.of(override));
//
//        List<AvailableSlotResponse> listOfAvailableSlots = slotGeneratorService.generateAvailableSlots(
//                responses, DATE, Collections.emptyList()
//        );
//
//        assertThat(listOfAvailableSlots).isEmpty();
//    }
//
//    @Test
//    void generateAvailableSlotsShouldReturnListOfCustomHoursSLots () {
//        ScheduleTemplateDTO template = templateBuilder(
//                "32daefcc8e0cafd8ceefdad3", "e1dd85d5-29e9-47bb-8124-9d558a5157cc", "MONDAY", "09:00",
//                "10:00", "12:00", "13:00", "30", null, null
//        );
//
//        ScheduleOverrideDTO override = new ScheduleOverrideDTO(
//                "beb22788-c5df-485b-9fb0-4484f6eb0d56", "9eba8aae-5a8d-47cb-b21b-f692037e01a5", DATE.toString(),
//                "CUSTOM_HOURS", "14:00", "15:00", "30", null);
//
//        ScheduleResponse responses = new ScheduleResponse(List.of(template), List.of(override));
//
//        List<AvailableSlotResponse> listOfAvailableSlots = slotGeneratorService.generateAvailableSlots(
//                responses, DATE, Collections.emptyList()
//        );
//
//        assertThat(listOfAvailableSlots).extracting(AvailableSlotResponse::startTime)
//                .contains("14:00", "14:30");
//    }
//
//    @Test
//    void generateAvailableSlotsShouldThrowWhenNoTemplatesFound () {
//        ScheduleTemplateDTO template = templateBuilder(
//                "32daefcc8e0cafd8ceefdad3", "e1dd85d5-29e9-47bb-8124-9d558a5157cc", "TUESDAY", "09:00",
//                "10:00", "12:00", "13:00", "30", null, null
//        );
//
//        ScheduleResponse responses = new ScheduleResponse(List.of(template), Collections.emptyList());
//
//        assertThatThrownBy(() -> slotGeneratorService.generateAvailableSlots(responses, DATE, Collections.emptyList()))
//                .isInstanceOf(NoSuchElementException.class);
//    }
//
//    private ScheduleTemplateDTO templateBuilder(
//            String id,
//            String doctorId,
//            String dayOfTheWeek,
//            String startTime,
//            String endTime,
//            String breakStartTime,
//            String breakEndTime,
//            String slotDurationOfMinutes,
//            String effectiveFrom,
//            String effectiveTo
//    ) {
//        return new ScheduleTemplateDTO(
//               id, doctorId, dayOfTheWeek, startTime, endTime, breakStartTime, breakEndTime, slotDurationOfMinutes, effectiveFrom, effectiveTo);
//    }
//}