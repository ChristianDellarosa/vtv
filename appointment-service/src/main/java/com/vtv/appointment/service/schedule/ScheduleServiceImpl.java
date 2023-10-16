package com.vtv.appointment.service.schedule;

import com.vtv.appointment.configuration.ScheduleConfiguration;
import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.domain.ScheduleQuery;
import com.vtv.appointment.model.dto.ScheduleQueryDto;
import com.vtv.appointment.repository.AppointmentRepository;
import com.vtv.appointment.service.schedule.filter.ScheduleByMonthAndDayAndHour;
import com.vtv.appointment.service.schedule.filter.ScheduleByMonthAndDayFilter;
import com.vtv.appointment.service.schedule.filter.ScheduleByMonthFilter;
import com.vtv.appointment.service.schedule.filter.ScheduleFilter;
import com.vtv.appointment.service.schedule.filter.ScheduleWithoutFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {

    private final AppointmentRepository appointmentRepository;

    private final ScheduleConfiguration scheduleConfiguration;

    public ScheduleServiceImpl(AppointmentRepository appointmentRepository, ScheduleConfiguration scheduleConfiguration) {
        this.appointmentRepository = appointmentRepository;
        this.scheduleConfiguration = scheduleConfiguration;
    }

    //TODO: Analizar si se tira un 400 o un vacio por las reglas de negocio, entiendo que un vacio.
    public List<ZonedDateTime> get(ScheduleQuery scheduleQuery) {
        final Pair<ZonedDateTime, ZonedDateTime> dateTimeRange = getScheduleFilter(scheduleQuery).find();

        final List<ZonedDateTime> notAvailableDateTimes = getNotAvailableDateTimes(dateTimeRange);

        final List<ZonedDateTime> availableAppointments = new ArrayList<>(); //TODO: No se deberían llamar appointments diferenciar turnos de disponibilidad
        //TODO: Ver como optimizar este for
        for (ZonedDateTime date = dateTimeRange.getFirst(); date.isBefore(dateTimeRange.getSecond()); date = date.plusHours(1)) {
            if (isAvailableDateTime(date) && isValidDate(date) && isValidTime(date.toLocalTime()) && !notAvailableDateTimes.contains(date)) {
                availableAppointments.add(date);
            }
        }
        return availableAppointments;
    }

    public Boolean isValidDate(ZonedDateTime date) {
        final var dateNow = ZonedDateTime.now();
        final var isDayAfterToday = dateNow.getDayOfYear() < date.getDayOfYear(); //No te permito sacar turno para el mismo dia
        final var isDateEnabled =
                scheduleConfiguration.getDaysEnable().contains(date.getDayOfWeek()) &&
                scheduleConfiguration.getMonthsEnable().contains(date.getMonth()) &&
                !scheduleConfiguration.getHolidayDays().contains(date.toLocalDate());

        return isDayAfterToday && isDateEnabled;
    }

    public Boolean isValidTime(LocalTime time) {
        return scheduleConfiguration.getHoursEnable().contains(time.getHour());
    }

    public Boolean isAvailableDateTime(ZonedDateTime date) {
        final var appointmentsInDateTimeQuantity = appointmentRepository.getByDateTime(date).size();
        return scheduleConfiguration.getPerHour() > appointmentsInDateTimeQuantity;
    }

    private ScheduleFilter getScheduleFilter(ScheduleQuery scheduleQuery) {
        final List<ScheduleFilter> strategies = List.of(
                new ScheduleByMonthAndDayAndHour(scheduleQuery),
                new ScheduleByMonthAndDayFilter(scheduleQuery),
                new ScheduleByMonthFilter(scheduleQuery),
                new ScheduleWithoutFilter(scheduleQuery)); //TODO: Analizar como hacerlo singleton, porque cada vez que llega aca estas instanciando 4 estrategias

        return strategies.stream()
                .filter(ScheduleFilter::canHandle)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("BAD REQUEST"));
    }
    private List<ZonedDateTime> getNotAvailableDateTimes(Pair<ZonedDateTime, ZonedDateTime> dateTimePair) {
        final List<Appointment> appointments = appointmentRepository.getByDateTimeRange(dateTimePair.getFirst(), dateTimePair.getSecond());

        final Function<ZonedDateTime, ZonedDateTime> key = dateTime -> dateTime; //TODO: Como reemplazar esto?
        final Map<ZonedDateTime, Long> appointmentsQuantityByDateTime = appointments.stream()
                .map(Appointment::getDateTime)
                .collect(Collectors.groupingBy(key, Collectors.counting()));

        final List<ZonedDateTime> notAvailableDateTimes = new ArrayList<>();
        //Lleno lista de turnos llenos
        for (Map.Entry<ZonedDateTime, Long> entry : appointmentsQuantityByDateTime.entrySet()) {
            if (scheduleConfiguration.getPerHour() <= entry.getValue().intValue()) {
                notAvailableDateTimes.add(entry.getKey());
            }
        }

        return notAvailableDateTimes;
    }
}
