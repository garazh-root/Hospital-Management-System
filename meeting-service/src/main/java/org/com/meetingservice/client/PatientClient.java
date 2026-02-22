package org.com.meetingservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "patient-service", fallback = PatientClientFallback.class
)
public interface PatientClient {


}
