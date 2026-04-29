package org.com.notificationservice.event.user;

import org.com.notificationservice.additional.Roles;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserRegisteredEvent(
        UUID id,
        String username,
        String email,
        Roles role,
        LocalDateTime registeredAt
) {}
