package org.com.doctorservice.service;

import lombok.RequiredArgsConstructor;
import org.com.doctorservice.additional.CustomDayOfTheWeek;
import org.com.doctorservice.dto.ScheduleTemplateRequest;
import org.com.doctorservice.dto.ScheduleTemplateResponse;
import org.com.doctorservice.exception.DoctorNotFoundException;
import org.com.doctorservice.exception.ScheduleNotFoundException;
import org.com.doctorservice.mapper.ScheduleMapper;
import org.com.doctorservice.messages.DoctorServiceMessages;
import org.com.doctorservice.model.ScheduleTemplate;
import org.com.doctorservice.repository.DoctorRepository;
import org.com.doctorservice.repository.ScheduleOverrideRepository;
import org.com.doctorservice.repository.ScheduleTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private ScheduleTemplateRepository scheduleTemplateRepository;
    private ScheduleOverrideRepository scheduleOverrideRepository;
    private ScheduleMapper scheduleMapper;
    private DoctorRepository doctorRepository;

    @Transactional
    public ScheduleTemplateResponse createScheduleTemplate(UUID doctorId, ScheduleTemplateRequest scheduleTemplateRequest) {
        doctorRepository.findById(doctorId).orElseThrow(() -> new DoctorNotFoundException(
                DoctorServiceMessages.DOCTOR_NOT_FOUND.getMessage()));

        ScheduleTemplate scheduleTemplate = ScheduleTemplate.builder()
                .doctorId(doctorId)
                .customDayOfTheWeek(CustomDayOfTheWeek.valueOf(scheduleTemplateRequest.getDayOfTheWeek()))
                .startTime(scheduleTemplateRequest.getStartTime())
                .endTime(scheduleTemplateRequest.getEndTime())
                .breakStartTime(scheduleTemplateRequest.getBreakStartTime())
                .breakEndTime(scheduleTemplateRequest.getBreakEndTime())
                .slotDurationOfMinutes(scheduleTemplateRequest.getSlotDuration())
                .effectiveFrom(scheduleTemplateRequest.getEffectiveFrom())
                .effectiveTo(scheduleTemplateRequest.getEffectiveTo())
                .active(true)
                .createdAt(LocalTime.now())
                .build();

        ScheduleTemplate savedScheduleTemplate = scheduleTemplateRepository.save(scheduleTemplate);

        return ScheduleMapper.toDTO(savedScheduleTemplate);

    }

    @Transactional
    public ScheduleTemplateResponse updateScheduleTemplate(UUID templateId, ScheduleTemplateRequest scheduleTemplateRequest) {
        ScheduleTemplate scheduleTemplate = scheduleTemplateRepository.findById(templateId).orElseThrow(
                () -> new ScheduleNotFoundException(DoctorServiceMessages.SCHEDULE_NOT_FOUND.getMessage())
        );

        scheduleTemplate.setStartTime(scheduleTemplateRequest.getStartTime());
        scheduleTemplate.setEndTime(scheduleTemplateRequest.getEndTime());
        scheduleTemplate.setBreakStartTime(scheduleTemplateRequest.getBreakStartTime());
        scheduleTemplate.setBreakEndTime(scheduleTemplateRequest.getBreakEndTime());
        scheduleTemplate.setSlotDurationOfMinutes(scheduleTemplateRequest.getSlotDuration());
        scheduleTemplate.setEffectiveFrom(scheduleTemplateRequest.getEffectiveFrom());
        scheduleTemplate.setEffectiveTo(scheduleTemplateRequest.getEffectiveTo());
        scheduleTemplate.setUpdatedAt(LocalTime.now());

        scheduleTemplateRepository.save(scheduleTemplate);

        return ScheduleMapper.toDTO(scheduleTemplate);
    }

    @Transactional
    public void deleteScheduleTemplate(UUID templateId) {
        ScheduleTemplate scheduleTemplate = scheduleTemplateRepository.findById(templateId).orElseThrow(
                () -> new ScheduleNotFoundException(DoctorServiceMessages.SCHEDULE_NOT_FOUND.getMessage())
        );

        scheduleTemplate.setActive(false);

        scheduleTemplateRepository.save(scheduleTemplate);
    }

    public List<ScheduleTemplateResponse> getScheduleTemplates(UUID doctorId) {
        return scheduleTemplateRepository.findByDoctorIdAndActiveTrue(doctorId)
                .stream()
                .map(ScheduleMapper::toDTO)
                .toList();
    }
}