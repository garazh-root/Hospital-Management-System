package org.com.doctorservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.com.doctorservice.additional.DoctorStatus;
import org.com.doctorservice.dto.DoctorRequestDTO;
import org.com.doctorservice.dto.DoctorResponseDTO;
import org.com.doctorservice.dto.validators.CreateDoctorValidationGroup;
import org.com.doctorservice.additional.Genders;
import org.com.doctorservice.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/doc")
@RequiredArgsConstructor
@Tag(name = "Doctor", description = "API for managing doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @Operation(summary = "Get doctors")
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponseDTO>> getAllDoctors() {
        List<DoctorResponseDTO> doctors = doctorService.getDoctors();

        return ResponseEntity.ok().body(doctors);
    }

    @Operation(summary = "Get doctor by id")
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> getDoctorById(@PathVariable UUID id) {
        DoctorResponseDTO doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok().body(doctor);
    }

    @Operation(summary = "Get doctors by specialization")
    @GetMapping("/filterBySpecialization")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctorsBySpecialization(@RequestParam String specialization) {
        List<DoctorResponseDTO> doctors = doctorService.getAllDoctorsBySpecialization(specialization);

        return ResponseEntity.ok().body(doctors);
    }

    @Operation(summary = "Get doctors by gender")
    @GetMapping("/filterByGender")
    public ResponseEntity<List<DoctorResponseDTO>> getDoctorsByGender(@RequestParam Genders gender) {
        List<DoctorResponseDTO> doctors = doctorService.findAllDoctorsByGender(gender);

        return ResponseEntity.ok().body(doctors);
    }

    @Operation(summary = "Save doctor")
    @PostMapping
    public ResponseEntity<DoctorResponseDTO> saveDoctor(@Validated({Default.class, CreateDoctorValidationGroup.class}) @RequestBody DoctorRequestDTO doctorRequestDTO) {
        DoctorResponseDTO response = doctorService.createDoctor(doctorRequestDTO);

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Update doctor")
    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> updateDoctor(@Validated({Default.class}) @PathVariable UUID id, @RequestBody DoctorRequestDTO doctorRequestDTO) {
        DoctorResponseDTO response = doctorService.updateDoctor(id, doctorRequestDTO);

        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "Delete doctor")
    @DeleteMapping("/{id}")
    public ResponseEntity<DoctorResponseDTO> deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteDoctor(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{doctorId}/status")
    public ResponseEntity<DoctorResponseDTO> updateDoctorStatus(
            @PathVariable UUID doctorId,
            @RequestParam DoctorStatus doctorStatus
    ) {
        DoctorResponseDTO response = doctorService.changeDoctorStatus(doctorId, doctorStatus);

        return ResponseEntity.ok().body(response);
    }
}