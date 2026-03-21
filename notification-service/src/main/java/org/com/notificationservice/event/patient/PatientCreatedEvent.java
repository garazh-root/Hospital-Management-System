package org.com.notificationservice.event.patient;

public record PatientCreatedEvent(
        String id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String status
) {}
