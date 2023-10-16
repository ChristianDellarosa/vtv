package com.vtv.appointment.controller;

import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.domain.AppointmentType;
import com.vtv.appointment.model.dto.AppointmentDto;
import com.vtv.appointment.model.dto.OrderType;
import com.vtv.appointment.model.dto.ScheduleQuery;
import com.vtv.appointment.model.dto.OrderInspectionDto;
import com.vtv.appointment.service.AppointmentService;
import com.vtv.appointment.service.InspectionPublisherService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@Slf4j
public class AppointmentController {

    private final InspectionPublisherService publisherService;

    private final AppointmentService appointmentService;

    public AppointmentController(InspectionPublisherService publisherService,
                                 AppointmentService appointmentService) {
        this.publisherService = publisherService;
        this.appointmentService = appointmentService;
    }


    @GetMapping("/hola/{id}")
    public void testPublicador(@PathVariable String id) {
        this.publisherService.send(OrderInspectionDto.builder()
                .carPlate("aCarplate")
                .clientEmail(id)
                .appointmentType(AppointmentType.RE_INSPECTION)
                .orderType(OrderType.CREATE)
                .build());
    }


    @PostMapping
    public AppointmentDto create(@Validated @RequestBody AppointmentDto appointmentDto) {
        log.info(appointmentDto.toString());
        appointmentService.create(Appointment.builder()
                .carPlate(appointmentDto.getCarPlate())
                .clientEmail(appointmentDto.getClientEmail())
                .dateTime(ZonedDateTime.of(appointmentDto.getDateTime(),  ZoneId.of("America/Buenos_Aires")))
                .type(appointmentDto.getType())
                .build());
        return null;
    }

    @GetMapping("/{id}")
    public AppointmentDto getByCarPlate(@RequestParam String id) { //TODO: Nuestro id es la patente, o quizas hacer por filter de email o carPlate
        log.info(id);
        final var hola = appointmentService.getLastByCarPlate(id);
        log.info(hola.toString());
        return null;
    }
//
//    @PutMapping
//    public AppointmentDto update(@Validated @RequestBody AppointmentDto appointmentDto) {
//        return null; //TODO: Deberia tener una etapa de confirmacion por email poneele
//    }
//

}
