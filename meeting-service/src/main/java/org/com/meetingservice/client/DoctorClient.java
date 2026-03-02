package org.com.meetingservice.client;

import org.com.meetingservice.dto.ScheduleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@FeignClient(
        name = "doctor-service",url = "${doctor.service.url:http://doctor-service:1011}", fallback = DoctorClientFallback.class
)
public interface DoctorClient {

    @GetMapping("/schedule/data")
    ScheduleResponse getSchedulesData (
            @RequestParam String doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
            );
}
