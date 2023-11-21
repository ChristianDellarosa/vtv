package com.vtv.appointment.controller;

import com.vtv.appointment.mapper.AppointmentDtoMapper;
import com.vtv.appointment.mapper.AppointmentMapper;
import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.domain.AppointmentType;
import com.vtv.appointment.model.dto.AppointmentDto;
import com.vtv.appointment.model.dto.OrderInspectionDto;
import com.vtv.appointment.model.dto.OrderType;
import com.vtv.appointment.service.AppointmentService;
import com.vtv.appointment.service.inspection.InspectionProducerService;
import com.vtv.appointment.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AppointmentDto create(@Validated @RequestBody AppointmentDto appointmentDto) {
        return AppointmentDtoMapper.toDto(appointmentService.create(AppointmentDtoMapper.toDomain(appointmentDto)));
    }

    @GetMapping("/{id}")
    public AppointmentDto getByCarPlate(@PathVariable String id) { //TODO: Nuestro id es la patente, o quizas hacer por filter de email o carPlate
        return AppointmentDtoMapper.toDto(appointmentService.getLastByCarPlate(id));
    }

}

//TODO: Agregar Swagger como docu