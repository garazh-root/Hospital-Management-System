package org.com.notificationservice.event.patient;

import org.com.notificationservice.additional.PatientStatus;

import java.util.UUID;

public record PatientStatusUpdatedEvent(
        UUID patientId,
        PatientStatus oldStatus,
        PatientStatus newStatus) {
}
