package org.com.meetingservice.dto;

import java.time.LocalTime;

public record ResolvedSchedule(
        boolean unavailable,
        LocalTime startTime,
        LocalTime endTime,
        LocalTime breakStartTime,
        LocalTime breakEndTime,
        int duration
) {
    public static ResolvedSchedule notAvailable() {
        return new ResolvedSchedule(true, null, null, null, null ,0);
    }

    public static ResolvedSchedule fromTemplate (
            LocalTime startTime,
            LocalTime endTime,
            LocalTime breakStartTime,
            LocalTime breakEndTime,
            int duration
    ) {
        return new ResolvedSchedule(false, startTime, endTime, breakStartTime, breakEndTime, duration);
    }

    public static ResolvedSchedule fromOverride (
            LocalTime startTime,
            LocalTime endTime,
            int duration
    ) {
        return new ResolvedSchedule(false, startTime, endTime, null, null, duration);
    }
}
