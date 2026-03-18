package org.com.doctorservice.events;

import org.com.doctorservice.additional.DoctorStatus;

import java.util.UUID;

public record DoctorCreatedEvent(
        UUID id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        DoctorStatus status
) {
}
