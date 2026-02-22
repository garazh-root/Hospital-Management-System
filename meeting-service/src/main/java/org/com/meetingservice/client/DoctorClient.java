package org.com.meetingservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "doctor-service", fallback = DoctorClientFallback.class
)
public interface DoctorClient {

}
