package org.com.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.com.notificationservice.additional.Roles;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendWelcomeEmail(String to, String username, Roles role) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setFrom(username);
        message.setSubject("Welcome to HealthApp!");

        String body = role.equals(Roles.DOCTOR)
                ? "Welcome Dr. " + username + ", your account is ready."
                : "Welcome " + username + ", you can now book appointments.";

        message.setText(body);

        mailSender.send(message);
    }
}