package org.com.notificationservice.event.doctor;

import org.com.notificationservice.additional.DoctorStatus;

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
