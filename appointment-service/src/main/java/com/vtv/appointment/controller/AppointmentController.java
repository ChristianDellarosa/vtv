package com.vtv.appointment.controller;

import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.domain.AppointmentType;
import com.vtv.appointment.model.dto.AppointmentDto;
import com.vtv.appointment.model.dto.DateTimeFilter;
import com.vtv.appointment.model.dto.OrderInspectionDto;
import com.vtv.appointment.service.AppointmentService;
import com.vtv.appointment.service.InspectionPublisherService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
                .type(AppointmentType.RE_INSPECTION)
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

    @GetMapping("/available") //TODO: By filters
    public List<Appointment> getAvailableAppointments(@Valid DateTimeFilter dateTimeFilter) { //TODO: Migrar a DTO
        return this.appointmentService.getAvailable(dateTimeFilter);
    }
//
//    @PostMapping
//    public AppointmentDto update(@Validated @RequestBody AppointmentDto appointmentDto) {
//        return null; //TODO: Deberia tener una etapa de confirmacion por email poneele
//    }
//
//    @PostMapping //TODO: Quizas no tenga sentido
//    public AppointmentDto getById(@Validated @RequestBody AppointmentDto appointmentDto) {
//        return null;
//    }
//
//    @GetMapping
//    public void getByFilter() { //TODO: By email or carPlate
//    }

}
