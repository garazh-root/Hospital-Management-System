package org.com.meetingservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.meetingservice.additional.MeetingStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDateTime;import java.util.UUID;

@Document(collection = "meeting")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meeting {

    @Id
    private String id;

    @Field(name = "doctor_id")
    @NotNull
    private UUID doctorId;

    @Field(name = "patient_id")
    @NotNull
    private UUID patientId;

    @Field(name = "meeting_date_time")
    @NotNull
    private LocalDateTime meetingDateTime;

    @Field(name = "duration_of_minutes")
    @NotNull
    private Integer durationOfMinutes;

    @Field(name = "status")
    @NotNull
    private MeetingStatus status;

    @Field(name = "reason")
    private String reason;

    @Field(name = "notes")
    private String notes;

    @CreatedDate
    @Field(name = "created_at")
    private Instant createdAt;

    @LastModifiedDate
    @Field(name = "updated_at")
    private Instant updatedAt;
}