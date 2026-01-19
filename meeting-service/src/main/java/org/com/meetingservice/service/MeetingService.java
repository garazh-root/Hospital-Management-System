package org.com.meetingservice.service;

import lombok.RequiredArgsConstructor;
import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.client.DoctorClient;
import org.com.meetingservice.client.PatientClient;
import org.com.meetingservice.exception.MeetingConflictException;
import org.com.meetingservice.exception.MeetingNotFoundException;
import org.com.meetingservice.messages.MeetingServiceMessages;
import org.com.meetingservice.requests.BookingRequest;
import org.com.meetingservice.dto.DoctorResponseDTO;
import org.com.meetingservice.dto.MeetingResponse;
import org.com.meetingservice.dto.PatientResponseDTO;
import org.com.meetingservice.mapper.MeetingMapper;
import org.com.meetingservice.model.Meeting;
import org.com.meetingservice.repository.MeetingRepository;
import org.com.meetingservice.requests.UpdateRequest;
import org.com.meetingservice.validation.DoctorValidation;
import org.com.meetingservice.validation.PatientValidation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeetingService {

    private final DoctorClient doctorClient;
    private final PatientClient patientClient;
    private final MeetingRepository meetingRepository;
    private final DoctorValidation doctorValidation;
    private final PatientValidation patientValidation;


    public MeetingResponse createMeeting(BookingRequest bookingRequest) {

        DoctorResponseDTO doctor = doctorClient.getDoctorById(bookingRequest.getDoctorId());
        PatientResponseDTO patient = patientClient.getPatientById(bookingRequest.getPatientId());

        patientValidation.checkPatientStatus(patient);
        doctorValidation.checkDoctorStatus(doctor);

        doctorValidation.checkDoctorSchedule(doctor, bookingRequest.getMeetingStartTime(), bookingRequest.getMeetingEndTime());

        doctorValidation.checkDoctorAvailability(
                bookingRequest.getDoctorId(), bookingRequest.getMeetingStartTime(), bookingRequest.getMeetingEndTime());
        patientValidation.checkPatientAvailability(
                bookingRequest.getPatientId(), bookingRequest.getMeetingStartTime(), bookingRequest.getMeetingEndTime()
        );

        Meeting meeting = MeetingMapper.toEntity(bookingRequest, doctor, patient);
        Meeting saved  = meetingRepository.save(meeting);

        return  MeetingMapper.toResponse(saved);
    }

    public List<MeetingResponse> findByPatientId(UUID patientId) {
        return mapList(meetingRepository.findByPatientId(patientId));
    }

    public List<MeetingResponse> findByDoctorId(UUID doctorId) {
        return mapList(meetingRepository.findByDoctorId(doctorId));
    }

    public List<MeetingResponse> findByStatus(MeetingStatus meetingStatus) {
        return mapList(meetingRepository.findByStatus(meetingStatus));
    }

    public MeetingResponse updateMeeting(String meetingId, UpdateRequest updateRequest) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
                () -> new MeetingNotFoundException(MeetingServiceMessages.MEETING_NOT_FOUND.getMessage()));

        if(updateRequest.getStatus() == MeetingStatus.CANCELLED) {
            throw new MeetingConflictException(MeetingServiceMessages.MEETING_CONFLICT.getMessage());
        }

        DoctorResponseDTO doctorResponse = doctorClient.getDoctorById(meeting.getDoctorId().toString());

        doctorValidation.checkDoctorAvailability(
                doctorResponse.getId(), updateRequest.getStartTime(), updateRequest.getEndTime());

        meeting.setDate(updateRequest.getDate());
        meeting.setStartTime(updateRequest.getStartTime());
        meeting.setEndTime(updateRequest.getEndTime());

        if(meeting.getStatus() != null){
            meeting.setStatus(meeting.getStatus());
        }

        Meeting updated = meetingRepository.save(meeting);

        return MeetingMapper.toResponse(updated);
    }

    public void cancelMeeting(String meetingId) {
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(
                () -> new MeetingNotFoundException(MeetingServiceMessages.MEETING_NOT_FOUND.getMessage())
        );

        if(meeting.getStatus() == MeetingStatus.CANCELLED) {
            throw new MeetingConflictException(MeetingServiceMessages.MEETING_CONFLICT.getMessage());
        }

        meeting.setStatus(MeetingStatus.CANCELLED);

        meetingRepository.save(meeting);
    }

    private List<MeetingResponse> mapList(List<Meeting> meetings) {
        return meetings.stream()
                .map(MeetingMapper::toResponse)
                .toList();
    }

}