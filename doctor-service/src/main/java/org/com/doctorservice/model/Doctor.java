package org.com.doctorservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.com.doctorservice.additional.DoctorStatus;
import org.com.doctorservice.additional.Genders;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "doctor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "doctor_id")
    private UUID doctorId;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "gender")
    private Genders gender;

    @NotNull
    @Email
    @Column(unique = true, name = "email")
    private String email;

    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotNull
    @Column(name = "specialization")
    private String specialization;

    @Column(precision = 2, scale = 1, name = "rating")
    @NotNull
    private BigDecimal rating;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "doctor_status")
    private DoctorStatus doctorStatus;

    @Column(name = "created_at")
    private LocalTime createdAt;

    @Column(name = "updated_at")
    private LocalTime updatedAt;
}