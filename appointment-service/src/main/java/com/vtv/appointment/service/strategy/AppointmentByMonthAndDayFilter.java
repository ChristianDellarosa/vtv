package com.vtv.appointment.service.strategy;

import com.vtv.appointment.model.dto.AppointmentQuery;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

public class AppointmentByMonthAndDayFilter extends AppointmentFilter {
    public AppointmentByMonthAndDayFilter(AppointmentQuery appointmentQuery) {
        super(appointmentQuery);
    }

    @Override
    public Boolean canHandle() {
        return Objects.nonNull(appointmentQuery.getMonth()) && Objects.nonNull(appointmentQuery.getDayNumber()) && Objects.isNull(appointmentQuery.getHour());
    }

    @Override
    public Pair<ZonedDateTime, ZonedDateTime> find() {
        //TODO: La hora tiene que estar predefinida por las properties, hora minima y hora maxima
        final ZonedDateTime firstDateValid = ZonedDateTime.of(LocalDate.of(2023, appointmentQuery.getMonth(), appointmentQuery.getDayNumber()).atTime(0, 0, 0), ZoneId.of("America/Buenos_Aires"));
        final ZonedDateTime lastDateValid = ZonedDateTime.of(LocalDate.of(2023, appointmentQuery.getMonth(), appointmentQuery.getDayNumber()).atTime(23, 59, 59), ZoneId.of("America/Buenos_Aires"));
        return Pair.of(firstDateValid, lastDateValid);
    }

}
