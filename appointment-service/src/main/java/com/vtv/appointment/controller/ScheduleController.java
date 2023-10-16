package com.vtv.appointment.controller;

import com.vtv.appointment.model.dto.ScheduleQuery;
import com.vtv.appointment.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@Slf4j
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public List<ZonedDateTime> getAvailableAppointments(@Valid ScheduleQuery scheduleQuery) { //TODO: Migrar a DTO
        return this.scheduleService.get(scheduleQuery);
    }
}
