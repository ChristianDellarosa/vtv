package com.vtv.appointment.service;

import com.vtv.appointment.configuration.AppointmentConfiguration;
import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.dto.AppointmentQuery;
import com.vtv.appointment.repository.AppointmentRepository;
import com.vtv.appointment.service.strategy.AppointmentByMonthAndDayAndHour;
import com.vtv.appointment.service.strategy.AppointmentByMonthAndDayFilter;
import com.vtv.appointment.service.strategy.AppointmentByMonthFilter;
import com.vtv.appointment.service.strategy.AppointmentFilter;
import com.vtv.appointment.service.strategy.AppointmentWithoutFilter;
import com.vtv.appointment.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final AppointmentConfiguration appointmentConfiguration;


    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, AppointmentConfiguration appointmentConfiguration) {
        this.appointmentRepository = appointmentRepository;
        this.appointmentConfiguration = appointmentConfiguration;
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
        //TODO: Buscar si esta persona no tiene otro turno de tipo INSPECTION, o si ya tiene turno en ese horario
    }

    //TODO: Aca yo tengo que exponer una lista de fechas, NO UNA LISTA DE APPOINTMENT UN APPOINTMENT ES CON DATOS DEL USUARIO
    @Override
    public List<ZonedDateTime> getAvailable(AppointmentQuery appointmentQuery) {
        //TODO: Analizar si se tira un 400 o un vacio por las reglas de negocio, entiendo que un vacio.
        final var dateTimeRange = getAppointmentFilter(appointmentQuery).find();
        final List<Appointment> notAvailableAppointments = appointmentRepository.getByDateTimeRange(dateTimeRange.getFirst(), dateTimeRange.getSecond());

        //FIXME: Usar aggregations para contar las fechas.

        //Cuento por hora la cantidad de turnos que hay, a futuro esto debería ser una agregacion
        Function<ZonedDateTime, ZonedDateTime> key = dateTime -> dateTime; //TODO: Como reemplazar esto?
        Map<ZonedDateTime, Long> cantidadFechas = notAvailableAppointments.stream()
                .map(Appointment::getDateTime)
                .collect(Collectors.groupingBy(key, Collectors.counting()));

        final List<ZonedDateTime> turnosLlenos = new ArrayList<>();
        //Lleno lista de turnos llenos
        for (Map.Entry<ZonedDateTime, Long> entry : cantidadFechas.entrySet()) {
            if (appointmentConfiguration.getPerHour().compareTo(entry.getValue().intValue()) <= 0) {
                turnosLlenos.add(entry.getKey());
                System.out.println(entry.getKey() + " - " + entry.getValue());
            }
        }


        //TODO: Un monton de estas validaciones deberían estar en el create tambien
        final List<ZonedDateTime> availableAppointments = new ArrayList<>(); //TODO: No se deberían llamar appointments diferenciar turnos de disponibilidad

        //TODO: Quizas esto debería estar en un servicio compartido
        for (ZonedDateTime date = dateTimeRange.getFirst(); date.isBefore(dateTimeRange.getSecond()); date = date.plusDays(1)) {
            if (appointmentConfiguration.getMonthsEnable().contains(date.getMonth())
                    && appointmentConfiguration.getDaysEnable().contains(date.getDayOfWeek())
            && !appointmentConfiguration.getHolidayDays().contains(date.toLocalDate())) {
                for (LocalTime hour = LocalTime.of(appointmentConfiguration.getFromHour(), 0, 0); hour.isBefore(LocalTime.of(appointmentConfiguration.getToHour(), 0, 0)); hour = hour.plusHours(1)) {
                    var auxDate = ZonedDateTime.of(date.toLocalDate(), hour, DateUtils.getZoneId());
                    if (appointmentConfiguration.getHoursEnable().contains(hour.getHour()) &&
                            !turnosLlenos.contains(auxDate)) {
                        availableAppointments.add(auxDate);
                    }
                }
            }
        }
        return availableAppointments;
    }

    private AppointmentFilter getAppointmentFilter(AppointmentQuery appointmentQuery) {
        final List<AppointmentFilter> strategies = List.of(
                new AppointmentByMonthAndDayAndHour(appointmentQuery),
                new AppointmentByMonthAndDayFilter(appointmentQuery),
                new AppointmentByMonthFilter(appointmentQuery),
                new AppointmentWithoutFilter(appointmentQuery)); //TODO: Analizar como hacerlo singleton, porque cada vez que llega aca estas instanciando 4 estrategias

        return strategies.stream()
                .filter(AppointmentFilter::canHandle)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("BAD REQUEST"));
    }
}


/*
    final ZonedDateTime firstDayOfMonth = ZonedDateTime.of(LocalDate.of(2023, 10, 1).with(TemporalAdjusters.firstDayOfMonth()).atTime(21, 0, 0), ZoneId.of("America/Buenos_Aires"));
    final ZonedDateTime lastDayOfMonth = ZonedDateTime.of(LocalDate.of(2023, 10, 1).with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59), ZoneId.of("America/Buenos_Aires")); //TODO: Busqueda para el mes*/
