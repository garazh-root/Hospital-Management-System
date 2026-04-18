package org.com.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.com.authservice.additional.Roles;


public record RegisterRequest(@NotBlank String username,
                              @NotBlank String password,
                              @NotBlank String confirmPassword,
                              @NotBlank String email,
                              @NotNull Roles role) {}
