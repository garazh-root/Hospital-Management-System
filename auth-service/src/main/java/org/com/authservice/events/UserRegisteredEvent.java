package org.com.authservice.events;

import org.com.authservice.additional.Roles;

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
