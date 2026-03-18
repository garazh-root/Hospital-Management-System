package org.com.doctorservice.events;

import org.com.doctorservice.additional.DoctorStatus;

import java.util.UUID;

public record DoctorChangedStatusEvent(
        UUID id,
        DoctorStatus oldStatus,
        DoctorStatus newStatus
) {
}
