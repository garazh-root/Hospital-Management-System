package org.com.meetingservice.client;

import lombok.extern.slf4j.Slf4j;
import org.com.meetingservice.dto.ScheduleResponse;
import org.com.meetingservice.exception.ServiceUnavailableException;
import org.com.meetingservice.messages.MeetingServiceMessages;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
public class DoctorClientFallback implements DoctorClient {

    public ScheduleResponse getSchedulesData(String doctorId, LocalDate startDate, LocalDate endDate) {
        log.error("Operation failed");
        throw new ServiceUnavailableException(MeetingServiceMessages.SERVICE_UNAVAILABLE.getMessage());
    }
}