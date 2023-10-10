package com.vtv.appointment.service.strategy;

import com.vtv.appointment.model.dto.AppointmentQuery;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

public class AppointmentByMonthAndDayAndHour extends AppointmentFilter {
    public AppointmentByMonthAndDayAndHour(AppointmentQuery appointmentQuery) {
        super(appointmentQuery);
    }

    @Override
    public Boolean canHandle() {
        return Objects.nonNull(appointmentQuery.getMonth())
                && Objects.nonNull(appointmentQuery.getDayNumber())
                && Objects.nonNull(appointmentQuery.getHour());

    }

    @Override
    public Pair<ZonedDateTime, ZonedDateTime> find() {
        //TODO: Falta validaciones de negocio de hora
        final ZonedDateTime firstDateValid = ZonedDateTime.of(LocalDate.of(2023, appointmentQuery.getMonth(), appointmentQuery.getDayNumber()).atTime(appointmentQuery.getHour(), 0, 0), ZoneId.of("America/Buenos_Aires"));
        final ZonedDateTime lastDateValid = ZonedDateTime.of(LocalDate.of(2023, appointmentQuery.getMonth(), appointmentQuery.getDayNumber()).atTime(appointmentQuery.getHour(), 59, 59), ZoneId.of("America/Buenos_Aires"));
        return Pair.of(firstDateValid, lastDateValid);
    }
}
