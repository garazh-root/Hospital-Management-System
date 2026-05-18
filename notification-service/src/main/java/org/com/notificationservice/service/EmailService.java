package org.com.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.com.notificationservice.additional.MeetingStatus;
import org.com.notificationservice.additional.Roles;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
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

    public void sendBookingEmail(String to, String username, Instant dateTime, int durationOfMinutes) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setFrom(username);
        message.setSubject("Your meeting has been booked!");

        LocalDateTime displayTime = LocalDateTime.ofInstant(dateTime, ZoneId.systemDefault());

        String body = "Greetings from HealthApp!\n" +
                "Meeting time : " + displayTime + "\n" +
                "Durations of minutes : " + durationOfMinutes + "\n" +
                "Have a nice day!";

        message.setText(body);

        mailSender.send(message);
    }

    public void sendBookingCancelledEmail(String to, String username, Instant dateTime) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setFrom(username);
        message.setSubject("Booking cancelled");

        LocalDateTime displayTime = LocalDateTime.ofInstant(dateTime, ZoneId.systemDefault());

        String body = "Greeting from HealthApp!\n" +
                "Meeting for : " + displayTime + "\n" +
                "Has been cancelled" + "\n" +
                "Have a nice day!";

        message.setText(body);

        mailSender.send(message);
    }
}