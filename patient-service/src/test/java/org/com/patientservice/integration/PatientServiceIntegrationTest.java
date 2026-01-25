package org.com.patientservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.patientservice.dto.PatientRequestDTO;
import org.com.patientservice.dto.PatientResponseDTO;
import org.com.patientservice.kafka.KafkaProducer;
import org.com.patientservice.model.genders.Gender;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class PatientServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private KafkaProducer kafkaProducer;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:12")
            .withDatabaseName("db1000")
            .withUsername("postgres")
            .withPassword("10105656");

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.sql.init.mode", () -> "always");
    }

    @Test
    public void fullCrudFlow() throws Exception {

        PatientRequestDTO request = PatientRequestDTO.builder()
                .firstName("Dima")
                .lastName("Nemo")
                .gender(Gender.MALE)
                .weight(80.3)
                .height(180.5)
                .email("test@mail.com")
                .phoneNumber("+3 (44) 2234 321")
                .dateOfBirth(LocalDate.parse("2000-01-01"))
                .address("ST test 4/3")
                .registeredDate(LocalDate.parse("2020-01-01"))
                .build();

        String createJson = objectMapper.writeValueAsString(request);

        String createPerson = mockMvc.perform(post("/patient")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn().getResponse().getContentAsString();

        PatientResponseDTO response =  objectMapper.readValue(createPerson, PatientResponseDTO.class);
        UUID id = UUID.fromString(response.getId());

        mockMvc.perform(get("/patient/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(request.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(request.getLastName()));

        mockMvc.perform(get("/patient/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists());

        response.setFirstName("Sasha");
        response.setPhoneNumber("+222222222");

        mockMvc.perform(put("/patient/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Sasha"))
                .andExpect(jsonPath("$.phoneNumber").value("+222222222"));

        mockMvc.perform(delete("/patient/{id}", id))
                .andExpect(status().isNoContent());
    }
}