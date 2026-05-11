package org.com.doctorservice.events;

import org.com.doctorservice.additional.Roles;

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
