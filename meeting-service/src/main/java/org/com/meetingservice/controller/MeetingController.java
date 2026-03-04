package org.com.meetingservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.dto.AvailableSlotResponse;
import org.com.meetingservice.dto.MeetingResponse;
import org.com.meetingservice.requests.MeetingRequest;
import org.com.meetingservice.service.MeetingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/meeting")
@Tag(name = "Meeting", description = "Api for booking meetings")
public class MeetingController {

    private MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @RequestMapping("/slots/{doctorId}")
    @Operation(summary = "Get available slots for doctor by doctor id")
    public ResponseEntity<List<AvailableSlotResponse>> getAvailableSlots(
            @PathVariable String doctorId,
            @RequestParam LocalDate date
    ) {
        List<AvailableSlotResponse> slots = meetingService.getAvailableSlots(doctorId, date);
        return ResponseEntity.ok().body(slots);
    }

    @PostMapping
    @Operation(summary = "Book a meeting")
    public ResponseEntity<MeetingResponse> bookMeeting(@RequestBody MeetingRequest meetingRequest) {
        MeetingResponse meetingResponse = meetingService.bookMeeting(meetingRequest);
        return ResponseEntity.ok().body(meetingResponse);
    }

    @GetMapping("/filterByDoctorIdAndDateBetween/{doctorId}")
    @Operation(summary = "Get meetings by doctor id and date between")
    public ResponseEntity<List<MeetingResponse>> findByDoctorIdAndDateTimeBetween(
            @PathVariable UUID doctorId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        List<MeetingResponse> meetingResponseList = meetingService.findByDoctorIdAndDateTimeBetween(doctorId, startDate, endDate);
        return ResponseEntity.ok().body(meetingResponseList);
    }

    @GetMapping("filterByDoctorIdAndDateBetweenAndStatus/{doctorId}")
    @Operation(summary = "Get meetings by doctor id and date between and status")
    public ResponseEntity<List<MeetingResponse>> findByDoctorIdAndDateTimeBetweenAndStatus(
            @PathVariable UUID doctorId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam MeetingStatus status
    ) {
        List<MeetingResponse> meetingResponseList = meetingService.findByDoctorIdAndDateTimeBetweenAndStatus(
                doctorId, startDate, endDate, status
        );
        return ResponseEntity.ok().body(meetingResponseList);
    }

    @GetMapping("/filterByPatientId/{patientId}")
    @Operation(summary = "Get meetings by patient id")
    public ResponseEntity<List<MeetingResponse>> findByPatientId(@PathVariable UUID patientId) {
        List<MeetingResponse> meetingResponseList = meetingService.findByPatientId(patientId);
        return ResponseEntity.ok().body(meetingResponseList);
    }

    @DeleteMapping("/{meetingId}")
    @Operation(summary = "Cancel meeting")
    public ResponseEntity<MeetingResponse> cancelMeeting(@PathVariable String meetingId) {
        meetingService.cancelMeeting(meetingId);
        return ResponseEntity.noContent().build();
    }
}