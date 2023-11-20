package com.vtv.appointment.service;

import com.vtv.appointment.exception.AppointmentAlreadyExistsException;
import com.vtv.appointment.exception.AppointmentNotFoundException;
import com.vtv.appointment.exception.InvalidAppointmentDateTimeException;
import com.vtv.appointment.exception.ScheduleErrorException;
import com.vtv.appointment.exception.commons.GenericDatabaseException;
import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.domain.AppointmentType;
import com.vtv.appointment.model.domain.commons.ErrorDetail;
import com.vtv.appointment.model.domain.commons.ExceptionError;
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

    private static final String CREATE_APPOINTMENT_ERROR_MESSAGE = "An error occurred while creating appointment.";
    private static final Integer CREATE_APPOINTMENT_ERROR_CODE = 305;

    private static final String APPOINTMENT_ALREADY_EXISTS_MESSAGE = "There is already a reserved slot for that vehicle for the day %s";
    private static final Integer APPOINTMENT_ALREADY_EXISTS_CODE = 306;

    private static final String INVALID_RE_APPOINTMENT_DATE_MESSAGE = "You cannot generate a shift prior to one already assigned.";
    private static final Integer INVALID_RE_APPOINTMENT_DATE_CODE = 307;

    private static final String INVALID_APPOINTMENT_DATE_MESSAGE = "The appointment date is not valid.";
    private static final Integer INVALID_APPOINTMENT_DATE_CODE = 307;

    private static final String FULL_BOOKING_APPOINTMENT_DATE_TIME_MESSAGE = "There are no appointments available for that date and time";
    private static final Integer FULL_BOOKING_APPOINTMENT_DATE_TIME_CODE = 308;

    private static final String APPOINTMENT_NOT_FOUND_MESSAGE = "Appointment not found by car plate";
    private static final Integer APPOINTMENT_NOT_FOUND_CODE = 309;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository,
                                  ScheduleService scheduleService,
                                  InspectionProducerService inspectionProducerService) {
        this.appointmentRepository = appointmentRepository;
        this.scheduleService = scheduleService;
        this.inspectionProducerService = inspectionProducerService;
    }

    @Override
    public Appointment create(Appointment appointment) {
        //TODO: FALTAN VALIDACIONES DE VALIDATORS JAVA
        //TODO: Revisar Logica ya que no puedo reservar un turno para hoy, entonces como voy a reparar algo que no es para hoy? como para hacer una demo
        //TODO: Analizar el orden de las validaciones
        log.info(appointment.toString());

        //1) Buscar si ya no tenia un turno de revision previo
        //TODO: BUG: No te deja sacar RE_INSPECCIONES
        final List<Appointment> appointmentsByCarPlate = getAppointmentsByCarPlate(appointment);

        if (appointment.isInspection()) {
            appointmentsByCarPlate.stream()
                    .filter(Appointment::isInspection)
                    .findFirst()
                    .ifPresent(ap -> {
                        log.info(APPOINTMENT_ALREADY_EXISTS_MESSAGE);
                        final var message = String.format(APPOINTMENT_ALREADY_EXISTS_MESSAGE, ap.getDateTime().toString());
                        throw new AppointmentAlreadyExistsException(
                                ExceptionError.builder()
                                        .description(message)
                                        .errorDetail(ErrorDetail.builder()
                                                .code(APPOINTMENT_ALREADY_EXISTS_CODE)
                                                .message(message)
                                                .build())
                                        .build());
                    }); //Checkea si tenia un turno asignado previamente
        }

        //2: Si es una re-reparacion, deberá chequearse que es posterior al turno de reparacion
        //   Si es una re-reparacion, y se asigna un nuevo turno (para re-re-repararlo) ese turno deberá ser posterior a la re-reparacion que ya tenia
        if (!isValidReInspectionAppointment(appointmentsByCarPlate, appointment)) {
            log.info(APPOINTMENT_ALREADY_EXISTS_MESSAGE);
            throw new InvalidAppointmentDateTimeException(
                    ExceptionError.builder()
                            .description(INVALID_RE_APPOINTMENT_DATE_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(INVALID_RE_APPOINTMENT_DATE_CODE)
                                    .message(INVALID_RE_APPOINTMENT_DATE_MESSAGE)
                                    .build())
                            .build());
        }

        //3) Buscar si la fecha es valida en nuestras condiciones
        if (!scheduleService.isValidDate(appointment.getDateTime()) || !scheduleService.isValidTime(appointment.getDateTime().toLocalTime())) {
            log.info(INVALID_APPOINTMENT_DATE_MESSAGE);
            throw new InvalidAppointmentDateTimeException(
                    ExceptionError.builder()
                            .description(INVALID_APPOINTMENT_DATE_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(INVALID_APPOINTMENT_DATE_CODE)
                                    .message(INVALID_APPOINTMENT_DATE_MESSAGE)
                                    .build())
                            .build());
        }

        //4) si hay turnos disponibles en base a la cantidad por horario
        if (!scheduleService.isAvailableDateTime(appointment.getDateTime())) {
            log.info(FULL_BOOKING_APPOINTMENT_DATE_TIME_MESSAGE);
            throw new InvalidAppointmentDateTimeException(
                    ExceptionError.builder()
                            .description(FULL_BOOKING_APPOINTMENT_DATE_TIME_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(FULL_BOOKING_APPOINTMENT_DATE_TIME_CODE)
                                    .message(FULL_BOOKING_APPOINTMENT_DATE_TIME_MESSAGE)
                                    .build())
                            .build());
        }

        //5 Reservar
        final Appointment appointmentCreated = createAppointment(appointment);

        //6 Mandar el evento
        inspectionProducerService.orderInspection(
                OrderInspectionDto.builder()
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
                .orElseThrow(() -> {
                    log.info(APPOINTMENT_NOT_FOUND_MESSAGE);
                    return new AppointmentNotFoundException(
                            ExceptionError.builder()
                                    .description(APPOINTMENT_NOT_FOUND_MESSAGE)
                                    .errorDetail(ErrorDetail.builder()
                                            .code(APPOINTMENT_NOT_FOUND_CODE)
                                            .message(APPOINTMENT_NOT_FOUND_MESSAGE)
                                            .build())
                                    .build());
                });
    }

    private Appointment createAppointment(Appointment appointment) {
        try {
            return appointmentRepository.create(appointment);
        } catch (GenericDatabaseException genericDatabaseException) {
            log.error(CREATE_APPOINTMENT_ERROR_MESSAGE, genericDatabaseException);
            throw new ScheduleErrorException(
                    ExceptionError.builder()
                            .description(CREATE_APPOINTMENT_ERROR_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(CREATE_APPOINTMENT_ERROR_CODE)
                                    .message(CREATE_APPOINTMENT_ERROR_MESSAGE)
                                    .build())
                            .build(), genericDatabaseException);
        }
    }

    private List<Appointment> getAppointmentsByCarPlate(Appointment appointment) {
        try {
            return appointmentRepository.getByCarPlate(appointment.getCarPlate());
        } catch (GenericDatabaseException genericDatabaseException) {
            log.error(CREATE_APPOINTMENT_ERROR_MESSAGE, genericDatabaseException);
            throw new ScheduleErrorException(
                    ExceptionError.builder()
                            .description(CREATE_APPOINTMENT_ERROR_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(CREATE_APPOINTMENT_ERROR_CODE)
                                    .message(CREATE_APPOINTMENT_ERROR_MESSAGE)
                                    .build())
                            .build(), genericDatabaseException);
        }

    }



    private Boolean isValidReInspectionAppointment(List<Appointment> appointmentsByCarPlate, Appointment actualAppointment) {

        if (!actualAppointment.isReinspection()) {
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