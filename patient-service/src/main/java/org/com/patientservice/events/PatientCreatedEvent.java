package org.com.patientservice.events;

public record PatientCreatedEvent(
        String id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String status
) {}
