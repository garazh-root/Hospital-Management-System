package org.com.meetingservice.service;

import org.com.meetingservice.dto.ResolvedSchedule;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class ResolvedScheduleTest {

    @Test
    void whenResolvedScheduleIsUnavailableItShouldReturnDTOWithNullValues() {
        ResolvedSchedule resolvedSchedule = ResolvedSchedule.notAvailable();

        assertThat(resolvedSchedule.unavailable()).isTrue();
        assertThat(resolvedSchedule.startTime()).isNull();
        assertThat(resolvedSchedule.endTime()).isNull();
    }

    @Test
    void fromTemplateShouldReturnNormalValuesThatWerePassedToTemplate() {
        ResolvedSchedule resolvedSchedule = ResolvedSchedule.fromTemplate(
                LocalTime.of(9, 0), LocalTime.of(13, 0),
                LocalTime.of(11, 0), LocalTime.of(12, 0),
                30
        );

        assertThat(resolvedSchedule.startTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(resolvedSchedule.endTime()).isEqualTo(LocalTime.of(13, 0));
    }

    @Test
    void fromOverrideShouldReturnNormalValuesThatWerePassedToOverride() {
        ResolvedSchedule resolvedSchedule = ResolvedSchedule.fromOverride(
                LocalTime.of(9, 0), LocalTime.of(13, 0), 30
        );

        assertThat(resolvedSchedule.startTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(resolvedSchedule.endTime()).isEqualTo(LocalTime.of(13, 0));
    }
}