package org.com.doctorservice.events;

import java.util.UUID;

public record DoctorDeletedEvent(UUID doctorId) {
}
