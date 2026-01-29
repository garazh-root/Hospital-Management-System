package org.com.doctorservice.integration;
//
//import org.com.doctorservice.additional.CustomDayOfTheWeek;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.UUID;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@Testcontainers
//@AutoConfigureMockMvc
public class ScheduleIntegrationTest {

}
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ScheduleRepository scheduleRepository;
//
//    @Container
//    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:12")
//            .withUsername("postgres")
//            .withPassword("10105656")
//            .withDatabaseName("dsdb");
//
//    @DynamicPropertySource
//    static void postgresProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
//        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
//        registry.add("spring.sql.init.mode", () -> "always");
//    }
//
//    @Test
//    void fullScheduleFlowTest() throws Exception {
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
//        UUID doctorId = UUID.randomUUID();
//
//        List<Schedule> schedules = List.of(firstSchedule, secondSchedule);
//
//        scheduleRepository.save(firstSchedule);
//        scheduleRepository.save(secondSchedule);
//
//        mockMvc.perform(get("/schedule/{doctorId}", doctorId))
//                .andExpect(status().isOk());
//
//
//        //TODO TestContainers not working(03.12)
//
//
//    }
//}