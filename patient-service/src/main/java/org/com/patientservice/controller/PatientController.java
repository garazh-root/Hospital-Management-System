package org.com.patientservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.com.patientservice.additional.PatientStatus;
import org.com.patientservice.dto.PatientRequestDTO;
import org.com.patientservice.dto.PatientResponseDTO;
import org.com.patientservice.dto.validators.CreatePatientValidationGroup;
import org.com.patientservice.service.PatientService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
@Tag(name = "Patient", description = "API for managing patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/patients")
    @Operation(summary = "Get Patients")
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patientResponseDTOList = patientService.getPatients();

        return ResponseEntity.ok().body(patientResponseDTOList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Patient By ID")
    public ResponseEntity<PatientResponseDTO> getPatientById(@PathVariable UUID id) {
        PatientResponseDTO patientResponseDTO = patientService.getPatientById(id);

        return ResponseEntity.ok().body(patientResponseDTO);
    }

    @PostMapping
    @Operation(summary = "Create Patient")
    public ResponseEntity<PatientResponseDTO> savePatient(@Validated({Default.class, CreatePatientValidationGroup.class})@RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO dto = patientService.createPatient(patientRequestDTO);

        return ResponseEntity.ok().body(dto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Patient")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id, @Validated({Default.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO dto = patientService.updatePatient(id, patientRequestDTO);

        return ResponseEntity.ok().body(dto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Patient")
    public ResponseEntity<PatientResponseDTO> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update patient status")
    public ResponseEntity<PatientResponseDTO> updatePatientStatus(
            @PathVariable UUID id,
            @RequestParam PatientStatus status
    ){
        PatientResponseDTO dto = patientService.changePatientStatus(id, status);

        return ResponseEntity.ok().body(dto);
    }

}