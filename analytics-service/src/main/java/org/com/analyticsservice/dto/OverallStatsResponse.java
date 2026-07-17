package org.com.analyticsservice.dto;

import java.util.List;

public record OverallStatsResponse(
        List<RegistrationStatsResponse> registrationStats,
        DailyMeetingStatsResponse dailyMeetingStatsList,
        Double cancellationRate
) {
}
