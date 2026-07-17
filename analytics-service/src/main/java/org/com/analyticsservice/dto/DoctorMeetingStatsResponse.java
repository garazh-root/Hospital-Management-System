package org.com.analyticsservice.dto;

import java.util.UUID;

public record DoctorMeetingStatsResponse(
        UUID doctorId, Integer bookedCount, Integer completedCount, Integer cancelledCount
) {
}
