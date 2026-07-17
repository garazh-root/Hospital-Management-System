package org.com.analyticsservice.dto;

import java.time.LocalDate;

public record DailyMeetingStatsResponse(
        LocalDate date, Integer bookedCount, Integer completedCount, Integer cancelledCount
) {
}
