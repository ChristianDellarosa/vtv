package com.vtv.appointment.controller;

import com.vtv.appointment.model.domain.ScheduleQuery;
import com.vtv.appointment.model.dto.ScheduleQueryDto;
import com.vtv.appointment.service.schedule.ScheduleService;
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
    public List<ZonedDateTime> getAvailableAppointments(@Valid ScheduleQueryDto scheduleQueryDto) {
        return this.scheduleService.get(
                ScheduleQuery.builder()
                .hour(scheduleQueryDto.getHour())
                .month(scheduleQueryDto.getMonth())
                .dayNumber(scheduleQueryDto.getDayNumber())
                .build()
        );
    }
}
