package org.com.meetingservice.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.meetingservice.additional.MeetingStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateRequest {

    @NotNull(message = "Date is required")
    @FutureOrPresent(message = "Date cannot be in past")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant date;

    @NotNull(message = "Start time is required")
    @FutureOrPresent(message = "Start time cannot be in past")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant startTime;

    @NotNull(message = "End time is required")
    @FutureOrPresent(message = "End time cannot be in past")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant endTime;

    @NotNull(message = "Meeting status is required")
    private MeetingStatus status;
}