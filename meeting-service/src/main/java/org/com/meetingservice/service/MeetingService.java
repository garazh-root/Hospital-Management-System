package org.com.meetingservice.service;

import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.client.DoctorClient;
import org.com.meetingservice.dto.AvailableSlotResponse;
import org.com.meetingservice.dto.ScheduleResponse;
import org.com.meetingservice.dto.MeetingResponse;
import org.com.meetingservice.exception.MeetingNotFoundException;
import org.com.meetingservice.mapper.MeetingMapper;
import org.com.meetingservice.messages.MeetingServiceMessages;
import org.com.meetingservice.model.Meeting;
import org.com.meetingservice.repository.MeetingRepository;
import org.com.meetingservice.requests.MeetingRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class MeetingService {
    private MeetingRepository meetingRepository;
    private SlotGeneratorService slotGeneratorService;
    private DoctorClient doctorClient;

    public MeetingService(MeetingRepository meetingRepository, SlotGeneratorService slotGeneratorService,  DoctorClient doctorClient) {
        this.meetingRepository = meetingRepository;
        this.slotGeneratorService = slotGeneratorService;
        this.doctorClient = doctorClient;
    }

    public List<AvailableSlotResponse> getAvailableSlots(String doctorId, LocalDate date) {
        ScheduleResponse doctorScheduleDataResponse = doctorClient.getSchedulesData(
                doctorId, date, date);

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Meeting> meetingList = meetingRepository.findScheduledMeetingsForDate(UUID.fromString(doctorId), start, end);

        List<LocalDateTime> bookedMeetings = meetingList
                .stream()
                .map(Meeting::getMeetingDateTime)
                .toList();

        return slotGeneratorService.generateAvailableSlots(doctorScheduleDataResponse, date, bookedMeetings);
    }

    public MeetingResponse bookMeeting(MeetingRequest meetingRequest) {
        LocalDate date = meetingRequest.meetingDateTime().toLocalDate();

        List<AvailableSlotResponse> availableSlots = getAvailableSlots(meetingRequest.doctorId().toString(), date);

        LocalTime requestedTime = meetingRequest.meetingDateTime().toLocalTime();

        boolean availableSlot = availableSlots.stream()
                .anyMatch(slot -> LocalTime.parse(slot.startTime()).equals(requestedTime));

        if (!availableSlot) {
            throw new IllegalStateException(MeetingServiceMessages.SLOT_NOT_AVAILABLE.getMessage());
        }

        Meeting meeting = Meeting.builder()
                .doctorId(meetingRequest.doctorId())
                .patientId(meetingRequest.patientId())
                .meetingDateTime(meetingRequest.meetingDateTime())
                .durationOfMinutes(meetingRequest.duration())
                .status(MeetingStatus.CONFIRMED)
                .reason(meetingRequest.reason())
                .notes("Regular checkup")
                .build();

        meetingRepository.save(meeting);

        return MeetingMapper.toResponseDTO(meeting);
    }

    public List<MeetingResponse> findByDoctorIdAndDateTimeBetween(UUID doctorId, LocalDate start, LocalDate end) {
        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(LocalTime.MAX);

        return mapList(meetingRepository.findByDoctorIdAndMeetingDateTimeBetween(doctorId, startDate, endDate));
    }

    public List<MeetingResponse> findByDoctorIdAndDateTimeBetweenAndStatus(UUID doctorId, LocalDate start, LocalDate end, MeetingStatus status) {
        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(LocalTime.MAX);

        return mapList(meetingRepository.findByDoctorIdAndMeetingDateTimeBetweenAndStatus(doctorId, startDate, endDate, status));
    }

    public List<MeetingResponse> findByPatientId(UUID patientId) {
        return mapList(meetingRepository.findByPatientId(patientId));
    }

    public void cancelMeeting(String meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new MeetingNotFoundException(meetingId));

        meeting.setStatus(MeetingStatus.CANCELLED);
        meeting.setUpdatedAt(Instant.now());

        meetingRepository.save(meeting);
    }

    public List<MeetingResponse> mapList(List<Meeting> meetings) {
        return meetings.stream()
                .map(MeetingMapper::toResponseDTO)
                .toList();
    }
}