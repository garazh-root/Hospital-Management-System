package org.com.notificationservice.event.doctor;

import org.com.notificationservice.additional.DoctorStatus;

import java.util.UUID;

public record DoctorChangedStatusEvent(
        UUID id,
        DoctorStatus oldStatus,
        DoctorStatus newStatus
) {
}
