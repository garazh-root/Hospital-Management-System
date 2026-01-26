package org.com.meetingservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.dto.MeetingResponse;
import org.com.meetingservice.model.Meeting;
import org.com.meetingservice.repository.MeetingRepository;
import org.com.meetingservice.requests.BookingRequest;
import org.com.meetingservice.requests.UpdateRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class MeetingServiceIntegrationTest {

    @Container
    public static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0")
            .withExposedPorts(27017);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MeetingRepository meetingRepository;

    private UUID patientId;
    private UUID doctorId;
    private String meetingId;
    private Meeting meeting;

    @BeforeEach
    void setUp() {
        patientId = UUID.randomUUID();
        doctorId = UUID.randomUUID();
        meetingId = "f4a27a8caa4c354ee18be994";

        meeting = Meeting.builder()
                .id(meetingId)
                .patientId(patientId)
                .doctorId(doctorId)
                .startTime(Instant.parse("2024-03-20T10:00:00Z"))
                .endTime(Instant.parse("2024-03-20T11:00:00Z"))
                .status(MeetingStatus.CONFIRMED)
                .notes("Regular checkup")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @AfterEach
    void tearDown() {
        meetingRepository.deleteAll();
    }

   //TODO Error with Docker env, after fix need to do integration test
}