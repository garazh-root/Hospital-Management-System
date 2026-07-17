package org.com.analyticsservice.dto;

import org.com.analyticsservice.addtitional.Roles;

public record RegistrationStatsResponse(
        Roles role, Integer count
) {
}
