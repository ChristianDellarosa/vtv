package com.vtv.appointment.controller;

import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.domain.AppointmentType;
import com.vtv.appointment.model.dto.AppointmentDto;
import com.vtv.appointment.model.dto.OrderInspectionDto;
import com.vtv.appointment.model.dto.OrderType;
import com.vtv.appointment.service.AppointmentService;
import com.vtv.appointment.service.inspection.InspectionProducerService;
import com.vtv.appointment.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

@RestController
@RequestMapping("/api/v1/appointments")
@Slf4j
public class AppointmentController {

    private final InspectionProducerService publisherService;

    private final AppointmentService appointmentService;

    public AppointmentController(InspectionProducerService publisherService,
                                 AppointmentService appointmentService) {
        this.publisherService = publisherService;
        this.appointmentService = appointmentService;
    }


    @GetMapping("/hola/{id}")
    public void testPublicador(@PathVariable String id) {
        this.publisherService.orderInspection(OrderInspectionDto.builder()
                .carPlate("aCarplate")
                .clientEmail(id)
                .dateTime(ZonedDateTime.now()) //TODO: NO Serializa correctamente la fecha
                .appointmentType(AppointmentType.RE_INSPECTION)
                .orderType(OrderType.CREATE)
                .build());
    }


    @PostMapping
    public AppointmentDto create(@Validated @RequestBody AppointmentDto appointmentDto) {
        log.info(appointmentDto.toString());
        final var appointment = appointmentService.create(Appointment.builder()
                .carPlate(appointmentDto.getCarPlate())
                .clientEmail(appointmentDto.getClientEmail())
                .dateTime(ZonedDateTime.of(appointmentDto.getDateTime(), DateUtils.getZoneId()))
                .type(appointmentDto.getType())
                .build());

        return AppointmentDto.builder()
                .carPlate(appointment.getCarPlate())
                .type(appointment.getType())
                .clientEmail(appointment.getClientEmail())
                .dateTime(appointment.getDateTime().toLocalDateTime())
                .build(); //TODO: Agregar mappers
    }

    @GetMapping("/{id}")
    public AppointmentDto getByCarPlate(@PathVariable String id) { //TODO: Nuestro id es la patente, o quizas hacer por filter de email o carPlate
        log.info(id);
        final var appointment = appointmentService.getLastByCarPlate(id);
        return AppointmentDto.builder()
                .carPlate(appointment.getCarPlate())
                .type(appointment.getType())
                .clientEmail(appointment.getClientEmail())
                .dateTime(appointment.getDateTime().toLocalDateTime())
                .build(); //TODO: Agregar mappers
    }

}
