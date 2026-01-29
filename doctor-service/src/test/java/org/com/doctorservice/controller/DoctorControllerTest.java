package org.com.doctorservice.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.com.doctorservice.additional.CustomDayOfTheWeek;
//import org.com.doctorservice.additional.DoctorStatus;
//import org.com.doctorservice.dto.DoctorRequestDTO;
//import org.com.doctorservice.dto.DoctorResponseDTO;
//import org.com.doctorservice.exception.EmptyComponentException;
//import org.com.doctorservice.exception.EmptyModelException;
//import org.com.doctorservice.additional.Genders;
//import org.com.doctorservice.mapper.DoctorMapper;
//import org.com.doctorservice.messages.DoctorServiceMessages;
//import org.com.doctorservice.service.DoctorService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.UUID;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

}
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    ObjectMapper objectMapper;
//
//    @MockitoBean
//    DoctorService doctorService;
//
//    @Test
//    @DisplayName("/GET, /doc/doctors - should return all doctors")
//    void getDoctorsShouldReturnAllDoctors() throws Exception {
//        ScheduleResponseDTO firstScheduleResponseDTO = scheduleResponseBuilder();
//
//        ScheduleResponseDTO secondScheduleResponseDTO = scheduleResponseBuilder();
//        secondScheduleResponseDTO.setDayOfWeek("TUESDAY");
//
//        ScheduleResponseDTO thirdScheduleResponseDTO = scheduleResponseBuilder();
//        thirdScheduleResponseDTO.setDayOfWeek("WEDNESDAY");
//
//        ScheduleResponseDTO fourthScheduleResponseDTO = scheduleResponseBuilder();
//        fourthScheduleResponseDTO.setDayOfWeek("THURSDAY");
//
//        DoctorResponseDTO firstResponse = DoctorResponseDTO.builder()
//                .id(UUID.randomUUID().toString())
//                .firstName("Jessy")
//                .lastName("Mohawk")
//                .gender(Genders.MALE.toString())
//                .email("test@mail.com")
//                .phoneNumber("+1 (321) 983453")
//                .specialization("Dermatologist")
//                .rating("9.1")
//                .doctorStatus("ACTIVE")
//                .scheduleList(List.of(firstScheduleResponseDTO, secondScheduleResponseDTO))
//                .build();
//
//        DoctorResponseDTO secondResponse = DoctorResponseDTO.builder()
//                .id(UUID.randomUUID().toString())
//                .firstName("Nolan")
//                .lastName("Barley")
//                .gender(Genders.MALE.toString())
//                .email("test@mail.ru")
//                .phoneNumber("+1 (440) 936453")
//                .specialization("Dermatologist")
//                .rating("9.1")
//                .doctorStatus("ACTIVE")
//                .scheduleList(List.of(thirdScheduleResponseDTO, fourthScheduleResponseDTO))
//                .build();
//
//        List<DoctorResponseDTO> models = List.of(firstResponse, secondResponse);
//
//        when(doctorService.getDoctors()).thenReturn(models);
//
//        mockMvc.perform(get("/doc/doctors"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].firstName").value(firstResponse.getFirstName()))
//                .andExpect(jsonPath("$[0].lastName").value(firstResponse.getLastName()))
//                .andExpect(jsonPath("$[0].gender").value(firstResponse.getGender()))
//                .andExpect(jsonPath("$[0].email").value(firstResponse.getEmail()))
//                .andExpect(jsonPath("$[0].phoneNumber").value(firstResponse.getPhoneNumber()))
//                .andExpect(jsonPath("$[0].specialization").value(firstResponse.getSpecialization()))
//                .andExpect(jsonPath("$[0].rating").value(firstResponse.getRating()))
//                .andExpect(jsonPath("$[0].doctorStatus").value(firstResponse.getDoctorStatus()))
//                .andExpect(jsonPath("$[0].scheduleList[0].dayOfWeek").value(firstScheduleResponseDTO.getDayOfWeek()))
//                .andExpect(jsonPath("$[0].scheduleList[1].dayOfWeek").value(secondScheduleResponseDTO.getDayOfWeek()))
//
//                .andExpect(jsonPath("$[1].firstName").value(secondResponse.getFirstName()))
//                .andExpect(jsonPath("$[1].lastName").value(secondResponse.getLastName()))
//                .andExpect(jsonPath("$[1].gender").value(secondResponse.getGender()))
//                .andExpect(jsonPath("$[1].email").value(secondResponse.getEmail()))
//                .andExpect(jsonPath("$[1].phoneNumber").value(secondResponse.getPhoneNumber()))
//                .andExpect(jsonPath("$[1].specialization").value(secondResponse.getSpecialization()))
//                .andExpect(jsonPath("$[1].rating").value(secondResponse.getRating()))
//                .andExpect(jsonPath("$[1].doctorStatus").value(secondResponse.getDoctorStatus()))
//                .andExpect(jsonPath("$[1].scheduleList[0].dayOfWeek").value(thirdScheduleResponseDTO.getDayOfWeek()))
//                .andExpect(jsonPath("$[1].scheduleList[1].dayOfWeek").value(fourthScheduleResponseDTO.getDayOfWeek()));
//
//        verify(doctorService).getDoctors();
//        verifyNoMoreInteractions(doctorService);
//    }
//
//    @Test
//    @DisplayName("/GET, /doc/{id} - should return doctor by id")
//    void getDoctorByIdShouldReturnDoctor() throws Exception {
//        ScheduleResponseDTO firstDoctorSchedule = scheduleResponseBuilder();
//
//        ScheduleResponseDTO secondDoctorSchedule = scheduleResponseBuilder();
//        secondDoctorSchedule.setDayOfWeek("WEDNESDAY");
//
//        UUID doctorId = UUID.randomUUID();
//
//        DoctorResponseDTO response = DoctorResponseDTO.builder()
//                .id(doctorId.toString())
//                .firstName("Nolan")
//                .lastName("Barley")
//                .gender(Genders.MALE.toString())
//                .email("test@mail.ru")
//                .phoneNumber("+1 (440) 936453")
//                .specialization("Dermatologist")
//                .rating("9.1")
//                .doctorStatus("ACTIVE")
//                .scheduleList(List.of(firstDoctorSchedule, secondDoctorSchedule))
//                .build();
//
//        when(doctorService.getDoctorById(doctorId)).thenReturn(response);
//
//        mockMvc.perform(get("/doc/{id}", doctorId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value(response.getFirstName()))
//                .andExpect(jsonPath("$.lastName").value(response.getLastName()))
//                .andExpect(jsonPath("$.gender").value(response.getGender()))
//                .andExpect(jsonPath("$.email").value(response.getEmail()))
//                .andExpect(jsonPath("$.phoneNumber").value(response.getPhoneNumber()))
//                .andExpect(jsonPath("$.specialization").value(response.getSpecialization()))
//                .andExpect(jsonPath("$.rating").value(response.getRating()))
//                .andExpect(jsonPath("$.doctorStatus").value(response.getDoctorStatus()))
//                .andExpect(jsonPath("$.scheduleList[0].dayOfWeek").value(response.getScheduleList().get(0).getDayOfWeek()))
//                .andExpect(jsonPath("$.scheduleList[1].dayOfWeek").value(response.getScheduleList().get(1).getDayOfWeek()));
//
//        verify(doctorService).getDoctorById(doctorId);
//        verifyNoMoreInteractions(doctorService);
//    }
//
//    @Test
//    @DisplayName("/GET, /doc/{id} - should throw exception when body is missing")
//    void getDoctorByIdShouldThrowEmptyModelExceptionWhenBodyIsMissing() throws Exception {
//        UUID doctorId = UUID.randomUUID();
//
//        when(doctorService.getDoctorById(doctorId)).thenThrow(EmptyModelException.class);
//
//        mockMvc.perform(get("/doc/{id}", doctorId))
//                .andExpect(status().isBadRequest());
//
//        verify(doctorService).getDoctorById(doctorId);
//        verifyNoMoreInteractions(doctorService);
//    }
//
//    @Test
//    @DisplayName("/GET, /doc/filterBySpecialization - should return doctors with specific specialization")
//    void getDoctorsBySpecializationShouldReturnDoctorsByRequestSpecialization() throws Exception {
//        String specialization = "Dermatologist";
//
//        ScheduleResponseDTO firstDoctorSchedule = scheduleResponseBuilder();
//
//        ScheduleResponseDTO secondDoctorSchedule = scheduleResponseBuilder();
//        secondDoctorSchedule.setDayOfWeek("WEDNESDAY");
//
//        ScheduleResponseDTO thirdDoctorSchedule = scheduleResponseBuilder();
//        thirdDoctorSchedule.setDayOfWeek("THURSDAY");
//
//        ScheduleResponseDTO fourthDoctorSchedule = scheduleResponseBuilder();
//        fourthDoctorSchedule.setDayOfWeek("FRIDAY");
//
//        DoctorResponseDTO response = DoctorResponseDTO.builder()
//                .id(UUID.randomUUID().toString())
//                .firstName("Rex")
//                .lastName("Flock")
//                .gender(Genders.MALE.toString())
//                .email("test@mail.com")
//                .phoneNumber("1 (233) 59 4344")
//                .specialization(specialization)
//                .rating("9.3")
//                .doctorStatus("ACTIVE")
//                .scheduleList(List.of(firstDoctorSchedule, secondDoctorSchedule))
//                .build();
//
//        DoctorResponseDTO responseDTO = DoctorResponseDTO.builder()
//                .id(UUID.randomUUID().toString())
//                .firstName("Alucard")
//                .lastName("Morgan")
//                .gender(Genders.MALE.toString())
//                .email("tes44t@mail.com")
//                .phoneNumber("1 (111) 59 4344")
//                .specialization(specialization)
//                .rating("9.9")
//                .doctorStatus("ACTIVE")
//                .scheduleList(List.of(thirdDoctorSchedule, fourthDoctorSchedule))
//                .build();
//
//        List<DoctorResponseDTO> models = List.of(response, responseDTO);
//
//        when(doctorService.getAllDoctorsBySpecialization(specialization)).thenReturn(models);
//
//        mockMvc.perform(get("/doc/filterBySpecialization")
//                        .param("specialization", specialization))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].specialization").value(specialization))
//                .andExpect(jsonPath("$[1].specialization").value(specialization));
//
//        verify(doctorService).getAllDoctorsBySpecialization(specialization);
//        verifyNoMoreInteractions(doctorService);
//    }
//
//    @Test
//    @DisplayName("GET, /doc/filterBySpecialization - should return empty list when no doctors")
//    void getDoctorsBySpecializationShouldReturnEmptyListWhenNoDoctors() throws Exception {
//        String specialization = "Oncologist";
//
//        when(doctorService.getAllDoctorsBySpecialization(specialization)).thenReturn(List.of());
//
//        mockMvc.perform(get("/doc/filterBySpecialization")
//                        .param("specialization", specialization))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(0))
//                .andExpect(content().json("[]"));
//
//        verify(doctorService).getAllDoctorsBySpecialization(specialization);
//        verifyNoMoreInteractions(doctorService);
//    }
//
//    @Test
//    @DisplayName("/GET, /doc/filterByGender - should return doctors with specific gender")
//    void getDoctorsByGenderShouldReturnDoctorsBtSpecificGender() throws Exception {
//        ScheduleResponseDTO firstDoctorSchedule = scheduleResponseBuilder();
//
//        ScheduleResponseDTO secondDoctorSchedule = scheduleResponseBuilder();
//        secondDoctorSchedule.setDayOfWeek("WEDNESDAY");
//
//        ScheduleResponseDTO thirdDoctorSchedule = scheduleResponseBuilder();
//        thirdDoctorSchedule.setDayOfWeek("THURSDAY");
//
//        ScheduleResponseDTO fourthDoctorSchedule = scheduleResponseBuilder();
//        fourthDoctorSchedule.setDayOfWeek("FRIDAY");
//
//        Genders gender = Genders.FEMALE;
//
//        DoctorResponseDTO firstResponse = DoctorResponseDTO.builder()
//                .id(UUID.randomUUID().toString())
//                .firstName("Sofia")
//                .lastName("Yasenitskaya")
//                .gender(gender.toString())
//                .email("test@mail.ru")
//                .phoneNumber("+8 (883) 23 3341")
//                .specialization("Dentist")
//                .rating("6.3")
//                .doctorStatus("ACTIVE")
//                .scheduleList(List.of(firstDoctorSchedule, secondDoctorSchedule))
//                .build();
//
//        DoctorResponseDTO secondResponse = DoctorResponseDTO.builder()
//                .id(UUID.randomUUID().toString())
//                .firstName("Anastasia")
//                .lastName("Melnyk")
//                .gender(gender.toString())
//                .email("test@mail.com")
//                .phoneNumber("+3 (068) 23 3391")
//                .specialization("Gastroenterologist")
//                .rating("8.9")
//                .doctorStatus("ACTIVE")
//                .scheduleList(List.of(thirdDoctorSchedule, fourthDoctorSchedule))
//                .build();
//
//        List<DoctorResponseDTO> models = List.of(firstResponse, secondResponse);
//
//        when(doctorService.findAllDoctorsByGender(gender)).thenReturn(models);
//
//        mockMvc.perform(get("/doc/filterByGender")
//                        .param("gender", gender.toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(2))
//                .andExpect(jsonPath("$[0].gender").value(gender.toString()))
//                .andExpect(jsonPath("$[1].gender").value(gender.toString()));
//
//        verify(doctorService).findAllDoctorsByGender(gender);
//        verifyNoMoreInteractions(doctorService);
//    }
//
//    @Test
//    @DisplayName("/GET, /doc/filterByGender - should return empty list if no doctors")
//    void getDoctorsByGenderShouldReturnEmptyListIfNoDoctors() throws Exception {
//        Genders gender = Genders.FEMALE;
//
//        when(doctorService.findAllDoctorsByGender(gender)).thenReturn(List.of());
//
//        mockMvc.perform(get("/doc/filterByGender")
//                        .param("gender", gender.toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()").value(0))
//                .andExpect(content().json("[]"));
//
//        verify(doctorService).findAllDoctorsByGender(gender);
//        verifyNoMoreInteractions(doctorService);
//    }
//
//    @Test
//    @DisplayName("/POST, /doc - should save doctor")
//    void saveDoctorShouldSaveDoctor() throws Exception {
//        ScheduleRequestDTO firstScheduleRequest = scheduleRequestBuilder();
//
//        ScheduleRequestDTO secondScheduleRequest = scheduleRequestBuilder();
//        secondScheduleRequest.setCustomDayOfTheWeek(CustomDayOfTheWeek.WEDNESDAY);
//
//        List<Schedule> scheduleList = DoctorMapper.toModelSchedule(List.of(firstScheduleRequest, secondScheduleRequest));
//
//        DoctorRequestDTO request = DoctorRequestDTO.builder()
//                .firstName("Carl")
//                .lastName("Edge")
//                .gender(Genders.MALE)
//                .email("test@mail.com")
//                .phoneNumber("+1 (440) 330 33219")
//                .specialization("Gynaecologist")
//                .rating(BigDecimal.valueOf(0.0))
//                .doctorStatus(DoctorStatus.ACTIVE)
//                .schedule(List.of(firstScheduleRequest, secondScheduleRequest))
//                .build();
//
//        DoctorResponseDTO response = DoctorResponseDTO.builder()
//                .id(UUID.randomUUID().toString())
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .gender(request.getGender().toString())
//                .email(request.getEmail())
//                .phoneNumber(request.getPhoneNumber())
//                .specialization(request.getSpecialization())
//                .rating(request.getRating().toString())
//                .doctorStatus(request.getDoctorStatus().toString())
//                .scheduleList(DoctorMapper.toScheduleDTO(scheduleList))
//                .build();
//
//        when(doctorService.createDoctor(any())).thenReturn(response);
//
//        mockMvc.perform(post("/doc")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(response.getId()))
//                .andExpect(jsonPath("$.firstName").value(request.getFirstName()))
//                .andExpect(jsonPath("$.lastName").value(request.getLastName()))
//                .andExpect(jsonPath("$.gender").value(request.getGender().toString()))
//                .andExpect(jsonPath("$.email").value(request.getEmail()))
//                .andExpect(jsonPath("$.phoneNumber").value(request.getPhoneNumber()))
//                .andExpect(jsonPath("$.specialization").value(request.getSpecialization()))
//                .andExpect(jsonPath("$.rating").value(request.getRating().toString()))
//                .andExpect(jsonPath("$.doctorStatus").value(request.getDoctorStatus().toString()))
//                .andExpect(jsonPath("$.scheduleList").isNotEmpty())
//                .andExpect(jsonPath("$.scheduleList[0].dayOfWeek").value(request.getSchedule().get(0).getCustomDayOfTheWeek().toString()))
//                .andExpect(jsonPath("$.scheduleList[1].dayOfWeek").value(request.getSchedule().get(1).getCustomDayOfTheWeek().toString()));
//
//        verify(doctorService).createDoctor(any());
//        verifyNoMoreInteractions(doctorService);
//    }
//
//    @Test
//    @DisplayName("/POST, /doc - should throw exception when field is empty")
//    void saveDoctorShouldReturnErrorResponseWhenOneOfRequestFieldIsMissing() throws Exception {
//        DoctorRequestDTO request = DoctorRequestDTO.builder()
//                .firstName("Carl")
//                .lastName(null)
//                .gender(null)
//                .email("test@mail.com")
//                .phoneNumber("+1 (440) 330 33219")
//                .specialization("Gynaecologist")
//                .rating(BigDecimal.valueOf(0.0))
//                .doctorStatus(DoctorStatus.ACTIVE)
//                .schedule(List.of())
//                .build();
//
//        when(doctorService.createDoctor(request)).thenThrow(new EmptyComponentException(DoctorServiceMessages.ARGUMENTS_NOT_VALID.getMessage()));
//
//        mockMvc.perform(post("/doc")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest());
//
//        verifyNoMoreInteractions(doctorService);
//    }
//
//    @Test
//    @DisplayName("/PUT, /doc/{id} - should return updated doctor")
//    void updateDoctorShouldReturnUpdatedDoctor() throws Exception {
//        ScheduleRequestDTO firstScheduleRequest = scheduleRequestBuilder();
//
//        ScheduleRequestDTO secondScheduleRequest = scheduleRequestBuilder();
//        secondScheduleRequest.setCustomDayOfTheWeek(CustomDayOfTheWeek.WEDNESDAY);
//
//        List<Schedule> scheduleList = DoctorMapper.toModelSchedule(List.of(firstScheduleRequest, secondScheduleRequest));
//
//        UUID doctorId = UUID.randomUUID();
//
//        DoctorRequestDTO request = DoctorRequestDTO.builder()
//                .firstName("Carl")
//                .lastName("Edge")
//                .gender(Genders.MALE)
//                .email("test@mail.com")
//                .phoneNumber("+1 (440) 330 33219")
//                .specialization("Gynaecologist")
//                .rating(BigDecimal.valueOf(0.0))
//                .doctorStatus(DoctorStatus.ACTIVE)
//                .schedule(List.of(firstScheduleRequest, secondScheduleRequest))
//                .build();
//
//        DoctorResponseDTO response = DoctorResponseDTO.builder()
//                .id(doctorId.toString())
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .gender(request.getGender().toString())
//                .email(request.getEmail())
//                .phoneNumber(request.getPhoneNumber())
//                .specialization(request.getSpecialization())
//                .rating(request.getRating().toString())
//                .doctorStatus(request.getDoctorStatus().toString())
//                .scheduleList(DoctorMapper.toScheduleDTO(scheduleList))
//                .build();
//
//        when(doctorService.updateDoctor(doctorId, request)).thenReturn(response);
//
//        mockMvc.perform(put("/doc/{id}", doctorId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value(request.getFirstName()))
//                .andExpect(jsonPath("$.lastName").value(request.getLastName()))
//                .andExpect(jsonPath("$.gender").value(request.getGender().toString()))
//                .andExpect(jsonPath("$.email").value(request.getEmail()))
//                .andExpect(jsonPath("$.phoneNumber").value(request.getPhoneNumber()))
//                .andExpect(jsonPath("$.specialization").value(request.getSpecialization()))
//                .andExpect(jsonPath("$.rating").value(request.getRating().toString()))
//                .andExpect(jsonPath("$.doctorStatus").value(request.getDoctorStatus().toString()))
//                .andExpect(jsonPath("$.scheduleList").isNotEmpty())
//                .andExpect(jsonPath("$.scheduleList[0].dayOfWeek").value(request.getSchedule().get(0).getCustomDayOfTheWeek().toString()))
//                .andExpect(jsonPath("$.scheduleList[1].dayOfWeek").value(request.getSchedule().get(1).getCustomDayOfTheWeek().toString()));
//
//        verify(doctorService).updateDoctor(doctorId, request);
//        verifyNoMoreInteractions(doctorService);
//    }
//
//    @Test
//    @DisplayName("/PUT, /doc/{id} - should throw error when one of request field is missing")
//    void updateDoctorShouldReturnErrorResponse() throws Exception {
//        UUID doctorId = UUID.randomUUID();
//
//        DoctorRequestDTO request = DoctorRequestDTO.builder()
//                .firstName("Carl")
//                .lastName("Edge")
//                .gender(Genders.MALE)
//                .email(null)
//                .phoneNumber("+1 (440) 330 33219")
//                .specialization("")
//                .rating(null)
//                .doctorStatus(DoctorStatus.INACTIVE)
//                .schedule(List.of())
//                .build();
//
//        mockMvc.perform(put("/doc/{id}", doctorId))
//                .andExpect(status().isBadRequest());
//
//        verifyNoMoreInteractions(doctorService);
//    }
//
//    @Test
//    @DisplayName("/DELETE, /doc/{id} - should delete doctor")
//    void deleteDoctorShouldReturnNoContentResponse() throws Exception {
//        UUID doctorId = UUID.randomUUID();
//
//        doNothing().when(doctorService).deleteDoctor(doctorId);
//
//        mockMvc.perform(delete("/doc/{id}", doctorId))
//                .andExpect(status().isNoContent());
//
//        verify(doctorService).deleteDoctor(doctorId);
//        verifyNoMoreInteractions(doctorService);
//    }
//
//    private ScheduleResponseDTO scheduleResponseBuilder() {
//        return ScheduleResponseDTO.builder()
//                .scheduleStartTime("09:00:00")
//                .scheduleEndTime("16:00:00")
//                .scheduleDate("2025-11-19")
//                .breakStartTime("13:00:00")
//                .breakEndTime("14:00:00")
//                .dayOfWeek("MONDAY")
//                .isDayOff("false")
//                .build();
//    }
//
//    private ScheduleRequestDTO scheduleRequestBuilder() {
//        return ScheduleRequestDTO.builder()
//                .scheduleStartTime(LocalTime.of(9, 0))
//                .scheduleEndTime(LocalTime.of(16, 0))
//                .scheduleDate(LocalDate.of(2025, 11, 20))
//                .breakStartTime(LocalTime.of(13, 0))
//                .breakEndTime(LocalTime.of(14, 0))
//                .customDayOfTheWeek(CustomDayOfTheWeek.TUESDAY)
//                .isDayOff(false)
//                .build();
//    }
//}