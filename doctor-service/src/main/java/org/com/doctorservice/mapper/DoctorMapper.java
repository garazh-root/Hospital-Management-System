package org.com.doctorservice.mapper;

import lombok.experimental.UtilityClass;
import org.com.doctorservice.additional.DoctorStatus;
import org.com.doctorservice.dto.DoctorRequestDTO;
import org.com.doctorservice.dto.DoctorResponseDTO;
import org.com.doctorservice.events.UserRegisteredEvent;
import org.com.doctorservice.exception.EmptyScheduleException;
import org.com.doctorservice.messages.DoctorServiceMessages;
import org.com.doctorservice.model.Doctor;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class DoctorMapper {
    public static DoctorResponseDTO toResponseDTO(Doctor doctor) {
        DoctorResponseDTO doctorResponseDTO = DoctorResponseDTO.builder()
                .id(doctor.getDoctorId().toString())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .gender(doctor.getGender().toString())
                .email(doctor.getEmail())
                .phoneNumber(doctor.getPhoneNumber())
                .specialization(doctor.getSpecialization())
                .rating(doctor.getRating().toString())
                .doctorStatus(doctor.getDoctorStatus().toString())
                .build();

        return doctorResponseDTO;
    }

    public static Doctor toModel(UserRegisteredEvent userRegisteredEvent) {
        Doctor doctor = Doctor.builder()
                .doctorId(userRegisteredEvent.id())
                .firstName(userRegisteredEvent.firstName())
                .lastName(userRegisteredEvent.lastName())
                .email(userRegisteredEvent.email())
                .phoneNumber(userRegisteredEvent.phoneNumber())
                .doctorStatus(DoctorStatus.INACTIVE)
                .build();

        return doctor;
    }
}