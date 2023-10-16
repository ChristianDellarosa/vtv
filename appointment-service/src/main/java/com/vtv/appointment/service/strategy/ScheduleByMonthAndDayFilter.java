package com.vtv.appointment.service.strategy;

import com.vtv.appointment.model.dto.ScheduleQuery;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public class ScheduleByMonthAndDayFilter extends ScheduleFilter {
    public ScheduleByMonthAndDayFilter(ScheduleQuery scheduleQuery) {
        super(scheduleQuery);
    }

    @Override
    public Boolean canHandle() {
        return Objects.nonNull(scheduleQuery.getMonth()) && Objects.nonNull(scheduleQuery.getDayNumber()) && Objects.isNull(scheduleQuery.getHour());
    }

    @Override
    public Pair<ZonedDateTime, ZonedDateTime> find() {
        //TODO: La hora tiene que estar predefinida por las properties, hora minima y hora maxima
        final ZonedDateTime firstDateValid = ZonedDateTime.of(LocalDate.of(2023, scheduleQuery.getMonth(), scheduleQuery.getDayNumber()).atTime(0, 0, 0), ZoneId.of("America/Buenos_Aires"));
        final ZonedDateTime lastDateValid = ZonedDateTime.of(LocalDate.of(2023, scheduleQuery.getMonth(), scheduleQuery.getDayNumber()).atTime(23, 59, 59), ZoneId.of("America/Buenos_Aires"));
        return Pair.of(firstDateValid, lastDateValid);
    }

}
