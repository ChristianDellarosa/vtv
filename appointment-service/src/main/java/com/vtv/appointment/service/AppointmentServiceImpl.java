package com.vtv.appointment.service;

import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.dto.DateTimeFilter;
import com.vtv.appointment.repository.AppointmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;


    public AppointmentServiceImpl(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public Appointment create(Appointment appointment) {
        final var app = appointmentRepository.create(appointment);
        log.info(app.toString());
        return appointment;
        //TODO: Busco si esa fecha es una fecha disponible en nuestras fechas (Analizar si dejamos una brecha de horario tipo de 9 a 20, o ponemos horarios fijos disponibles, tipo 14: 00hs, 15: 00 hs etc)
        //TODO: Busco si para la fecha y hora que el quiere reservar, hay turnos disponibles en base a la cantidad
        //TODO: Si esta Ok, busco si ya no tenia un turno De revision anteriormente, si tiene Error
        //TODO: Si esta todo ok -> Reservo
        //TODO: Mando el evento
    }

    @Override
    public List<Appointment> getAvailable(DateTimeFilter dateTimeFilter) {
        final ZonedDateTime firstDayOfMonth = ZonedDateTime.of(LocalDate.of(2023, 10, 1).with(TemporalAdjusters.firstDayOfMonth()).atTime(21, 0, 0), ZoneId.of("America/Buenos_Aires"));
        final ZonedDateTime lastDayOfMonth = ZonedDateTime.of(LocalDate.of(2023, 10, 1).with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59), ZoneId.of("America/Buenos_Aires")); //TODO: Busqueda para el mes
        return appointmentRepository.getByDateTimeRange(firstDayOfMonth, lastDayOfMonth);
    }
}
