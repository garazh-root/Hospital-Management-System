package org.com.authservice.service;

import lombok.RequiredArgsConstructor;
import org.com.authservice.additional.AuthServiceMessages;
import org.com.authservice.dto.LoginRequest;
import org.com.authservice.dto.JwtResponse;
import org.com.authservice.dto.RegisterRequest;
import org.com.authservice.exception.EmailAlreadyExistsException;
import org.com.authservice.jwt.JwtService;
import org.com.authservice.model.User;
import org.com.authservice.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtResponse login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(AuthServiceMessages.BAD_CREDENTIALS.getMessage());
        }

        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(
                        () -> new UsernameNotFoundException(AuthServiceMessages.USER_NOT_FOUND.getMessage())
                );

        String token = jwtService.generateToken(user);

        return new JwtResponse(token);
    }

    public JwtResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new EmailAlreadyExistsException(AuthServiceMessages.EMAIL_ALREADY_EXISTS.getMessage());
        }

        if (!registerRequest.password().equals(registerRequest.confirmPassword())) {
            throw new BadCredentialsException(AuthServiceMessages.PASSWORD_NOT_MATCH.getMessage());
        }

        if (userRepository.findByUsername(registerRequest.username()).isPresent()) {
            throw new UsernameNotFoundException(AuthServiceMessages.USER_NOT_FOUND.getMessage());
        }

        User user = User.builder()
                .username(registerRequest.username())
                .password(passwordEncoder.encode(registerRequest.password()))
                .email(registerRequest.email())
                .role(registerRequest.role())
                .build();

        userRepository.save(user);

        return new JwtResponse(jwtService.generateToken(user));
    }
}