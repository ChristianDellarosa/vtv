package com.vtv.appointment.service.schedule;

import com.vtv.appointment.configuration.ScheduleConfiguration;
import com.vtv.appointment.exception.ScheduleErrorException;
import com.vtv.appointment.exception.commons.GenericDatabaseException;
import com.vtv.appointment.exception.ScheduleFilterException;
import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.domain.ScheduleQuery;
import com.vtv.appointment.model.domain.commons.ErrorDetail;
import com.vtv.appointment.model.domain.commons.ExceptionError;
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

    private static final int ONE = 1;

    public static final String FILTER_COMBINATION_NOT_EXISTS_MESSAGE = "The request filter combination is invalid.";
    public static final Integer FILTER_COMBINATION_NOT_EXISTS_NOT_EXISTS_CODE = 310;

    public static final String GET_SCHEDULE_ERROR_MESSAGE = "An error occurred while obtaining the schedules.";
    public static final Integer GET_SCHEDULE_ERROR_CODE = 320;

    public ScheduleServiceImpl(AppointmentRepository appointmentRepository, ScheduleConfiguration scheduleConfiguration) {
        this.appointmentRepository = appointmentRepository;
        this.scheduleConfiguration = scheduleConfiguration;
    }

    public List<ZonedDateTime> get(ScheduleQuery scheduleQuery) {
        final Pair<ZonedDateTime, ZonedDateTime> dateTimeRange = getScheduleFilter(scheduleQuery).find();

        final List<ZonedDateTime> notAvailableDateTimes = getNotAvailableDateTimes(dateTimeRange);

        final List<ZonedDateTime> availableSchedules = new ArrayList<>();

        //TODO: Ver como optimizar este for
        for (ZonedDateTime date = dateTimeRange.getFirst(); date.isBefore(dateTimeRange.getSecond()); date = date.plusHours(ONE)) {
            if (isAvailableDateTime(date) && isValidDate(date) && isValidTime(date.toLocalTime()) && !notAvailableDateTimes.contains(date)) {
                availableSchedules.add(date);
            }
        }
        return availableSchedules;
    }

    public Boolean isValidDate(ZonedDateTime date) {
        final var dateNow = ZonedDateTime.now();
        final var isDayAfterOrEqualToday = dateNow.getDayOfYear() <= date.getDayOfYear(); //TODO: Delete = for Demo
        final var isDateEnabled =
                scheduleConfiguration.getDaysEnable().contains(date.getDayOfWeek()) &&
                        scheduleConfiguration.getMonthsEnable().contains(date.getMonth()) &&
                        !scheduleConfiguration.getHolidayDays().contains(date.toLocalDate());

        return isDayAfterOrEqualToday && isDateEnabled;
    }

    public Boolean isValidTime(LocalTime time) {
        return scheduleConfiguration.getHoursEnable().contains(time.getHour());
    }

    public Boolean isAvailableDateTime(ZonedDateTime date) {
        final var appointmentsInDateTimeQuantity = getAppointmentsByDateTime(date).size();
        return scheduleConfiguration.getPerHour() > appointmentsInDateTimeQuantity;
    }

    private ScheduleFilter getScheduleFilter(ScheduleQuery scheduleQuery) {
        final List<ScheduleFilter> strategies = List.of(
                new ScheduleByMonthAndDayAndHour(scheduleQuery),
                new ScheduleByMonthAndDayFilter(scheduleQuery),
                new ScheduleByMonthFilter(scheduleQuery),
                new ScheduleWithoutFilter(scheduleQuery));
        //TODO: Analizar como hacerlo singleton, porque cada vez que llega aca estas instanciando 4 estrategias

        return strategies.stream()
                .filter(ScheduleFilter::canHandle)
                .findFirst()
                .orElseThrow(() -> {
                    log.info(FILTER_COMBINATION_NOT_EXISTS_MESSAGE);
                    return new ScheduleFilterException(
                            ExceptionError.builder()
                                    .description(FILTER_COMBINATION_NOT_EXISTS_MESSAGE)
                                    .errorDetail(ErrorDetail.builder()
                                            .code(FILTER_COMBINATION_NOT_EXISTS_NOT_EXISTS_CODE)
                                            .message(FILTER_COMBINATION_NOT_EXISTS_MESSAGE)
                                            .build())
                                    .build());
                });
    }

    private List<ZonedDateTime> getNotAvailableDateTimes(Pair<ZonedDateTime, ZonedDateTime> dateTimePair) {
        final List<Appointment> appointments = getAppointmentsByDateTimeRange(dateTimePair);

        final Function<ZonedDateTime, ZonedDateTime> key = dateTime -> dateTime;
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

    private List<Appointment> getAppointmentsByDateTimeRange(Pair<ZonedDateTime, ZonedDateTime> dateTimePair) {
        try {
            return appointmentRepository.getByDateTimeRange(dateTimePair.getFirst(), dateTimePair.getSecond());
        } catch (GenericDatabaseException genericDatabaseException) {
            log.error(GET_SCHEDULE_ERROR_MESSAGE, genericDatabaseException);
            throw new ScheduleErrorException(
                    ExceptionError.builder()
                            .description(GET_SCHEDULE_ERROR_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(GET_SCHEDULE_ERROR_CODE)
                                    .message(GET_SCHEDULE_ERROR_MESSAGE)
                                    .build())
                            .build(), genericDatabaseException);
        }
    }

    private List<Appointment> getAppointmentsByDateTime(ZonedDateTime date) {
        try {
            return appointmentRepository.getByDateTime(date);
        } catch (GenericDatabaseException genericDatabaseException) {
            log.error(GET_SCHEDULE_ERROR_MESSAGE, genericDatabaseException);
            throw new ScheduleErrorException(
                    ExceptionError.builder()
                            .description(GET_SCHEDULE_ERROR_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(GET_SCHEDULE_ERROR_CODE)
                                    .message(GET_SCHEDULE_ERROR_MESSAGE)
                                    .build())
                            .build(), genericDatabaseException);
        }
    }
}
