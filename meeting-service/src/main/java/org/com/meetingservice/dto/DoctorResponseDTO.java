package org.com.meetingservice.dto;

public record DoctorResponseDTO
        (
                String id,
                String firstName,
                String lastName,
                String gender,
                String email,
                String phoneNumber,
                String specialization,
                String rating,
                String doctorStatus) {
}