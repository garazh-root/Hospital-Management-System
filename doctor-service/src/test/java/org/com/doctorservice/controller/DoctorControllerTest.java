package org.com.doctorservice.controller;

import org.com.doctorservice.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.doctorservice.additional.DoctorStatus;
import org.com.doctorservice.dto.DoctorRequestDTO;
import org.com.doctorservice.dto.DoctorResponseDTO;
import org.com.doctorservice.additional.Genders;
import org.com.doctorservice.service.DoctorService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DoctorService doctorService;

    private Doctor doctor;
    private DoctorResponseDTO doctorResponseDTO;
    private DoctorRequestDTO doctorRequestDTO;

    private UUID doctorId;

    @BeforeEach
    void setUp() {
        doctorId = UUID.randomUUID();

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
    void getDoctorsShouldSuccessfullyReturnListOFDoctorResponseDTOs() throws Exception {
        List<DoctorResponseDTO> doctorResponseDTOS = List.of(doctorResponseDTO);

        when(doctorService.getDoctors()).thenReturn(doctorResponseDTOS);

        mockMvc.perform(get("/doc/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(doctorResponseDTOS.get(0).getId()));

        verify(doctorService).getDoctors();
        verify(doctorService, times(1)).getDoctors();
    }

    @Test
    void getDoctorsShouldReturnEmptyListIfNoDoctorIsPresent() throws Exception {
        when(doctorService.getDoctors()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/doc/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(doctorService).getDoctors();
        verify(doctorService, times(1)).getDoctors();
    }

    @Test
    void getDoctorByIdShouldReturnDoctorResponse() throws Exception {
        when(doctorService.getDoctorById(doctorId)).thenReturn(doctorResponseDTO);

        mockMvc.perform(get("/doc/{id}", doctorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(doctorResponseDTO.getId()));

        verify(doctorService).getDoctorById(doctorId);
        verify(doctorService, times(1)).getDoctorById(doctorId);
    }

    @Test
    void getDoctorBySpecializationShouldReturnListOfDoctors() throws Exception {
        List<DoctorResponseDTO> doctorResponseDTOS = List.of(doctorResponseDTO);

        when(doctorService.getAllDoctorsBySpecialization("Cardiologist")).thenReturn(doctorResponseDTOS);

        mockMvc.perform(get("/doc/filterBySpecialization")
                        .param("specialization", "Cardiologist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(doctorResponseDTOS.get(0).getId()));

        verify(doctorService).getAllDoctorsBySpecialization("Cardiologist");
        verify(doctorService, times(1)).getAllDoctorsBySpecialization("Cardiologist");
    }

    @Test
    void getDoctorBySpecializationShouldReturnEmptyListIfNoDoctorIsPresent() throws Exception {
        when(doctorService.getAllDoctorsBySpecialization("Cardiologist")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/doc/filterBySpecialization")
                        .param("specialization", "Cardiologist"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(doctorService).getAllDoctorsBySpecialization("Cardiologist");
        verify(doctorService, times(1)).getAllDoctorsBySpecialization("Cardiologist");
    }

    @Test
    void getDoctorByGenderShouldReturnListOfDoctors() throws Exception {
        List<DoctorResponseDTO> doctorResponseDTOS = List.of(doctorResponseDTO);

        when(doctorService.findAllDoctorsByGender(Genders.MALE)).thenReturn(List.of(doctorResponseDTO));

        mockMvc.perform(get("/doc/filterByGender")
                        .param("gender", Genders.MALE.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(doctorResponseDTOS.get(0).getId()));

        verify(doctorService).findAllDoctorsByGender(Genders.MALE);
        verify(doctorService, times(1)).findAllDoctorsByGender(Genders.MALE);
    }

    @Test
    void getDoctorByGenderShouldReturnEmptyListIfNoDoctorIsPresent() throws Exception {
        when(doctorService.findAllDoctorsByGender(Genders.MALE)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/doc/filterByGender")
                        .param("gender", Genders.MALE.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(doctorService).findAllDoctorsByGender(Genders.MALE);
        verify(doctorService, times(1)).findAllDoctorsByGender(Genders.MALE);
    }

    @Test
    void crateDoctorShouldSuccessfullyInvokePostMethod() throws Exception {
        when(doctorService.createDoctor(doctorRequestDTO)).thenReturn(doctorResponseDTO);

        mockMvc.perform(post("/doc")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(doctorResponseDTO.getId()))
                .andExpect(jsonPath("$.specialization").value(doctorResponseDTO.getSpecialization()))
                .andExpect(jsonPath("$.email").value(doctorResponseDTO.getEmail()));

        verify(doctorService).createDoctor(doctorRequestDTO);
        verify(doctorService, times(1)).createDoctor(doctorRequestDTO);
    }

    @Test
    void updateDoctorShouldSuccessfullyInvokePutMethod() throws Exception {
        when(doctorService.updateDoctor(doctorId, doctorRequestDTO)).thenReturn(doctorResponseDTO);

        mockMvc.perform(put("/doc/{id}", doctorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(doctorResponseDTO.getId()))
                .andExpect(jsonPath("$.specialization").value(doctorResponseDTO.getSpecialization()))
                .andExpect(jsonPath("$.email").value(doctorResponseDTO.getEmail()));

        verify(doctorService).updateDoctor(doctorId, doctorRequestDTO);
        verify(doctorService, times(1)).updateDoctor(doctorId, doctorRequestDTO);
    }

    @Test
    void deleteDoctorShouldSuccessfullyInvokeDeleteMethod() throws Exception {
        doNothing().when(doctorService).deleteDoctor(doctorId);

        mockMvc.perform(delete("/doc/{id}", doctorId))
                .andExpect(status().isNoContent());

        verify(doctorService).deleteDoctor(doctorId);
        verify(doctorService, times(1)).deleteDoctor(doctorId);
    }
}
