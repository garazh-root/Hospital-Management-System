package org.com.meetingservice.repository;

import org.com.meetingservice.additional.MeetingStatus;
import org.com.meetingservice.model.Meeting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Repository
public interface MeetingRepository extends MongoRepository<Meeting, String> {

    List<Meeting> findByDoctorIdAndMeetingDateTimeBetween(UUID doctorId, Instant start, Instant end);

    List<Meeting> findByDoctorIdAndMeetingDateTimeBetweenAndStatus(UUID doctorId, Instant start, Instant end, MeetingStatus status);

    List<Meeting> findByPatientId(UUID patientId);

    @Query("{'doctor_id' : ?0, 'meeting_date_time' : {$gte: ?1, $lt: ?2}, 'status': 'CONFIRMED'}")
    List<Meeting> findScheduledMeetingsForDate(
            UUID doctorId,
            Instant startOfTheDay,
            Instant endOfTheDay
    );

    List<Meeting> findByStatus(MeetingStatus status);
}
