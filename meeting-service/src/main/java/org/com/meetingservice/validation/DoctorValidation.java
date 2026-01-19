package org.com.meetingservice.validation;

import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.dto.DoctorResponseDTO;
import org.com.meetingservice.dto.ScheduleResponseDTO;
import org.com.meetingservice.exception.*;
import org.com.meetingservice.messages.MeetingServiceMessages;
import org.com.meetingservice.model.Meeting;
import org.com.meetingservice.repository.MeetingRepository;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.List;
import java.util.UUID;

@Component
public class DoctorValidation {

    private MeetingRepository meetingRepository;

    public DoctorValidation(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public void checkDoctorStatus(DoctorResponseDTO doctor) {
        if(doctor == null || !doctor.getDoctorStatus().equals("ACTIVE")) {
            throw new InvalidStatusException(MeetingServiceMessages.INVALID_STATUS.getMessage());
        }
    }

    public void checkDoctorSchedule(DoctorResponseDTO doctor, Instant start, Instant end) {
        if(doctor.getSchedulesList() == null || doctor.getSchedulesList().isEmpty()) {
            throw new ScheduleNotAvailableException(MeetingServiceMessages.SCHEDULE_NOT_AVAILABLE.getMessage());
        }

        ZonedDateTime startZdt = start.atZone(ZoneId.systemDefault());
        ZonedDateTime endZdt = end.atZone(ZoneId.systemDefault());

        LocalDate matchingDate = startZdt.toLocalDate();
        LocalTime matchingStartTime = startZdt.toLocalTime();
        LocalTime matchingEndTime = endZdt.toLocalTime();
        DayOfWeek matchingDayOfWeek = matchingDate.getDayOfWeek();

        ScheduleResponseDTO matchingSchedule = doctor.getSchedulesList().stream()
                .filter(schedule -> matchesDay(schedule, matchingDayOfWeek, matchingDate))
                .findFirst()
                .orElseThrow(() -> new ScheduleNotMatchingException(MeetingServiceMessages.SCHEDULE_NOT_MATCHING.getMessage()));

        if("true".equals(matchingSchedule.getIsDayOff())) {
            throw new ScheduleNotAvailableException(MeetingServiceMessages.SCHEDULE_NOT_AVAILABLE.getMessage());
        }

        LocalTime scheduleStart = LocalTime.parse(matchingSchedule.getScheduleStartTime());
        LocalTime scheduleEnd = LocalTime.parse(matchingSchedule.getScheduleEndTime());

        if(matchingStartTime.isBefore(scheduleStart) || matchingEndTime.isAfter(scheduleEnd)) {
            throw new ScheduleNotAvailableException(MeetingServiceMessages.SCHEDULE_NOT_AVAILABLE.getMessage());
        }

        if(matchingSchedule.getBreakStartTime() != null && matchingSchedule.getBreakEndTime() != null) {
            LocalTime breakStartTime = LocalTime.parse(matchingSchedule.getBreakStartTime());
            LocalTime breakEndTime = LocalTime.parse(matchingSchedule.getBreakEndTime());

            if(timeOverlapForLocalTime(matchingStartTime, matchingEndTime, breakStartTime, breakEndTime)) {
                throw new InvalidMeetingException(MeetingServiceMessages.INVALID_MEETING_EXCEPTION.getMessage());
            }
        }
    }

    private boolean matchesDay(ScheduleResponseDTO schedule, DayOfWeek dayOfWeek, LocalDate date) {
        if(schedule != null && !schedule.getScheduleDate().isBlank()){
            try{
                LocalDate scheduleDate = LocalDate.parse(schedule.getScheduleDate());
                if(scheduleDate.equals(date)){
                    return true;
                }
            }catch (Exception e){
                throw new ScheduleNotMatchingException(MeetingServiceMessages.SCHEDULE_NOT_MATCHING.getMessage());
            }
        }

        if(schedule.getDayOfWeek() != null){
            return schedule.getDayOfWeek().equals(dayOfWeek) ||
                    schedule.getDayOfWeek().equals(dayOfWeek.name());
        }
        return false;
    }

    public void checkDoctorAvailability(String doctorId, Instant start, Instant end) {
        List<Meeting> conflicts = meetingRepository.findByDoctorIdAndStatusAndStartTimeBetween(
                UUID.fromString(doctorId), MeetingStatus.CONFIRMED, start.minus(Duration.ofHours(4)), end.plus(Duration.ofHours(4))
        );

        boolean conflictFound = conflicts.stream()
                .anyMatch(d -> timeOverlapForInstant(start, end, d.getStartTime(), d.getEndTime()));

        if(conflictFound) {
            throw new MeetingConflictException(MeetingServiceMessages.MEETING_CONFLICT.getMessage());
        }
    }

    private boolean timeOverlapForLocalTime(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private boolean timeOverlapForInstant(Instant start1, Instant end1, Instant start2, Instant end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}