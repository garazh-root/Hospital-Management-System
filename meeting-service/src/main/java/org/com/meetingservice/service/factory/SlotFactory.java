package org.com.meetingservice.service.factory;

import org.com.meetingservice.dto.AvailableSlotResponse;
import org.com.meetingservice.dto.ResolvedSchedule;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class SlotFactory {

    public List<AvailableSlotResponse> generateSlots(LocalDate date, ResolvedSchedule schedule) {
        List<AvailableSlotResponse> slots = new ArrayList<>();
        LocalTime current = schedule.startTime();
        int duration = schedule.duration();

        while(!current.plusMinutes(duration).isAfter(schedule.endTime())) {
            if(!duringBreakTime(current, duration, schedule)){
                slots.add(new AvailableSlotResponse(
                        date.toString(),
                        current.toString(),
                        current.plusMinutes(duration).toString(),
                        Integer.toString(duration)
                ));
            }
            current = current.plusMinutes(duration);
        }
        return slots;
    }

    private boolean duringBreakTime(LocalTime current, int duration, ResolvedSchedule schedule) {
        if(schedule.breakStartTime() == null || schedule.breakEndTime() == null) {
            return false;
        }
        return current.isBefore(schedule.breakEndTime()) &&
                current.plusMinutes(duration).isAfter(schedule.breakStartTime());
    }
}