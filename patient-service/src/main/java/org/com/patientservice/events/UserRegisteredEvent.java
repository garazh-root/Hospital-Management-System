package org.com.patientservice.events;

import org.com.patientservice.additional.Roles;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID id,
        String username,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Roles role,
        LocalDateTime registeredAt
) {
}
