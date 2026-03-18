package org.com.patientservice.events;

import org.com.patientservice.additional.PatientStatus;

import java.util.UUID;

public record PatientStatusUpdatedEvent(
        UUID patientId,
        PatientStatus oldStatus,
        PatientStatus newStatus) {
}
