package com.vtv.appointment.service;

import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.domain.AppointmentType;
import com.vtv.appointment.model.dto.OrderInspectionDto;
import com.vtv.appointment.model.dto.OrderType;
import com.vtv.appointment.repository.AppointmentRepository;
import com.vtv.appointment.service.schedule.ScheduleService;
import com.vtv.appointment.service.inspection.InspectionProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final ScheduleService scheduleService;

    private final InspectionProducerService inspectionProducerService;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  ScheduleService scheduleService,
                                  InspectionProducerService inspectionProducerService) {
        this.appointmentRepository = appointmentRepository;
        this.scheduleService = scheduleService;
        this.inspectionProducerService = inspectionProducerService;
    }

    @Override
    public Appointment create(Appointment appointment) {
        //TODO: Revisar Logica ya que no puedo reservar un turno para hoy, entonces como voy a reparar algo que no es para hoy? como para hacer una demo
        //TODO: Analizar el orden de las validaciones
        log.info(appointment.toString());

        //1) Buscar si ya no tenia un turno de revision previo
        //TODO: BUG: No te deja sacar RE_INSPECCIONES
        final var appointmentsByCarPlate = appointmentRepository.getByCarPlate(appointment.getCarPlate());

        if(appointment.isInspection()) {
            appointmentsByCarPlate.stream()
                    .filter(Appointment::isInspection)
                    .findFirst().ifPresent(appointment1 -> {
                        throw new  RuntimeException(""); //Todo: usted tiene un turno asignado previamente para el dia ...
                    }); //Checkea si tenia un turno asignado previamente
        }

        //2: Si es una re-reparacion, deberá chequearse que es posterior al turno de reparacion
        //   Si es una re-reparacion, y se asigna un nuevo turno (para re-re-repararlo) ese turno deberá ser posterior a la re-reparacion que ya tenia
        if(!isValidReInspectionAppointment(appointmentsByCarPlate, appointment)) {
            //THROW ERROR
            throw new  RuntimeException("");
        }

        //3) Buscar si la fecha es valida en nuestras condiciones
        if(!scheduleService.isValidDate(appointment.getDateTime()) || !scheduleService.isValidTime(appointment.getDateTime().toLocalTime())) {
            //THROW: ERROR: El turno no es valido 400
            throw new  RuntimeException("");
        }

        //4) si hay turnos disponibles en base a la cantidad por horario
        if(!scheduleService.isAvailableDateTime(appointment.getDateTime())) {
            //THROW: ERROR: El turno no esta disponible 409
            throw new  RuntimeException("");
        }

        //5 Reservar
        final Appointment appointmentCreated = appointmentRepository.create(appointment);

        //6 Mandar el evento
        inspectionProducerService.orderInspection(OrderInspectionDto.builder()
                        .clientEmail(appointmentCreated.getClientEmail())
                        .carPlate(appointmentCreated.getCarPlate())
                        .dateTime(appointmentCreated.getDateTime())
                        .orderType(OrderType.CREATE)
                        .appointmentType(appointmentCreated.getType())
                .build());

        return appointmentCreated;
    }

    @Override
    public Appointment getLastByCarPlate(String carPlate) { //TODO: Analizar si tiene sentido budscar todos o el ultimo
        return appointmentRepository.getByCarPlate(carPlate)
                .stream()
                .max(Comparator.comparing((Appointment::getDateTime))) //TODO: El ultimo turno
                .orElseThrow(() -> new RuntimeException("NOT FOUND"));
    }

    private Boolean isValidReInspectionAppointment(List<Appointment> appointmentsByCarPlate, Appointment actualAppointment) {

        if(!actualAppointment.isReinspection()) {
            return Boolean.TRUE;
        }

        final var hasPreviousInspection = appointmentsByCarPlate.stream()
                .filter(ap -> AppointmentType.INSPECTION.equals(ap.getType()))
                .map(Appointment::getDateTime)
                .allMatch(dateTime -> dateTime.getDayOfYear() < actualAppointment.getDateTime().getDayOfYear());

        final var hasPreviousReinspection = appointmentsByCarPlate.stream()
                .filter(ap -> AppointmentType.RE_INSPECTION.equals(ap.getType()))
                .map(Appointment::getDateTime)
                .allMatch(dateTime -> dateTime.getDayOfYear() < actualAppointment.getDateTime().getDayOfYear());

        return hasPreviousInspection && hasPreviousReinspection;
    }
}