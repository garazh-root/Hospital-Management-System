package org.com.doctorservice.service;

import org.com.doctorservice.additional.CustomDayOfTheWeek;
import org.com.doctorservice.additional.OverrideType;
import org.com.doctorservice.dto.*;
import org.com.doctorservice.exception.DoctorNotFoundException;
import org.com.doctorservice.exception.OverrideAlreadyExistsException;
import org.com.doctorservice.exception.ScheduleNotFoundException;
import org.com.doctorservice.mapper.ScheduleMapper;
import org.com.doctorservice.messages.DoctorServiceMessages;
import org.com.doctorservice.model.ScheduleOverride;
import org.com.doctorservice.model.ScheduleTemplate;
import org.com.doctorservice.repository.DoctorRepository;
import org.com.doctorservice.repository.ScheduleOverrideRepository;
import org.com.doctorservice.repository.ScheduleTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService {

    private ScheduleTemplateRepository scheduleTemplateRepository;
    private ScheduleOverrideRepository scheduleOverrideRepository;
    private DoctorRepository doctorRepository;

    @Autowired
    public ScheduleService(
            ScheduleTemplateRepository scheduleTemplateRepository, ScheduleOverrideRepository scheduleOverrideRepository, DoctorRepository doctorRepository) {
        this.scheduleTemplateRepository = scheduleTemplateRepository;
        this.scheduleOverrideRepository = scheduleOverrideRepository;
        this.doctorRepository = doctorRepository;
    }

    @Transactional
    public ScheduleTemplateResponse createScheduleTemplate(UUID doctorId, ScheduleTemplateRequest scheduleTemplateRequest) {
        if(!doctorRepository.existsById(doctorId)) {
            throw new DoctorNotFoundException(DoctorServiceMessages.DOCTOR_NOT_FOUND.getMessage());
        }

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
                .createdAt(LocalDate.now())
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
        scheduleTemplate.setUpdatedAt(LocalDate.now());

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

    public List<ScheduleTemplateResponse> getSchedulesByDoctorIdDayOfTheWeek(UUID doctorId, CustomDayOfTheWeek dayOfWeek) {
        return scheduleTemplateRepository.findByDoctorIdAndCustomDayOfTheWeekAndActiveTrue(doctorId, dayOfWeek)
                .stream()
                .map(ScheduleMapper::toDTO)
                .toList();
    }

    @Transactional
    public ScheduleOverrideResponse createScheduleOverride(UUID doctorId, ScheduleOverrideRequest scheduleOverrideRequest) {
       if(!doctorRepository.existsById(doctorId)) {
           throw new DoctorNotFoundException(DoctorServiceMessages.DOCTOR_NOT_FOUND.getMessage());
       }

        List<ScheduleOverride> conflictOverrides = scheduleOverrideRepository.findByDoctorIdAndDate(doctorId, scheduleOverrideRequest.getDate());
        if(!conflictOverrides.isEmpty()) {
            throw new OverrideAlreadyExistsException(DoctorServiceMessages.OVERRIDE_ALREADY_EXISTS.getMessage());
        }

        ScheduleOverride scheduleOverride = ScheduleOverride.builder()
                .doctorId(doctorId)
                .date(scheduleOverrideRequest.getDate())
                .overrideType(OverrideType.valueOf(scheduleOverrideRequest.getOverrideType()))
                .startTime(scheduleOverrideRequest.getStartTime())
                .endTime(scheduleOverrideRequest.getEndTime())
                .slotDurationOfMinutes(scheduleOverrideRequest.getSlotDurationOfMinutes())
                .reason(scheduleOverrideRequest.getReason())
                .createdAt(LocalDate.now())
                .build();

        ScheduleOverride savedScheduleOverride = scheduleOverrideRepository.save(scheduleOverride);

        return ScheduleMapper.toDTO(savedScheduleOverride);
    }

    @Transactional
    public void deleteScheduleOverride(UUID overrideId) {
        ScheduleOverride scheduleOverride = scheduleOverrideRepository.findById(overrideId).orElseThrow(
                () -> new ScheduleNotFoundException(DoctorServiceMessages.SCHEDULE_NOT_FOUND.getMessage())
        );

        scheduleOverrideRepository.delete(scheduleOverride);
    }

    public List<ScheduleOverrideResponse> getScheduleOverridesByDoctorIdAndDateBetween(UUID doctorId, LocalDate startDate, LocalDate endDate) {
        return scheduleOverrideRepository.findByDoctorIdAndDateBetween(doctorId, startDate, endDate)
                .stream()
                .map(ScheduleMapper::toDTO)
                .toList();
    }

    public List<ScheduleOverrideResponse> getScheduleOverridesByDoctorId(UUID doctorId) {
        return scheduleOverrideRepository.findByDoctorId(doctorId)
                .stream()
                .map(ScheduleMapper::toDTO)
                .toList();
    }

    public ScheduleResponse getSchedulesData(UUID doctorId, LocalDate startDate, LocalDate endDate) {
        List<ScheduleTemplate> scheduleTemplateList = scheduleTemplateRepository.findByDoctorIdAndActiveTrue(doctorId);

        List<ScheduleTemplateResponse> scheduleTemplateResponseList = scheduleTemplateList.stream()
                .filter(f -> isEffectiveDuring(f, startDate, endDate))
                .map(ScheduleMapper::toDTO)
                .toList();

        List<ScheduleOverrideResponse> overrides = scheduleOverrideRepository
                .findByDoctorIdAndDateBetween(doctorId, startDate, endDate)
                .stream()
                .map(ScheduleMapper::toDTO)
                .toList();

        return ScheduleResponse.builder()
                .listOfScheduleTemplateResponse(scheduleTemplateResponseList)
                .listOfScheduleOverrideResponse(overrides)
                .build();
    }

    private boolean isEffectiveDuring(ScheduleTemplate scheduleTemplate, LocalDate startDate, LocalDate endDate) {
        LocalDate effectiveFrom = scheduleTemplate.getEffectiveFrom();
        LocalDate effectiveTo = scheduleTemplate.getEffectiveTo();

        if(effectiveFrom == null && effectiveTo == null) {
            return true;
        }

        boolean startsBeforeEnd = effectiveFrom == null || !effectiveFrom.isAfter(endDate);
        boolean endsAfterStart = effectiveTo == null || !effectiveTo.isBefore(startDate);

        return startsBeforeEnd && endsAfterStart;
    }
}