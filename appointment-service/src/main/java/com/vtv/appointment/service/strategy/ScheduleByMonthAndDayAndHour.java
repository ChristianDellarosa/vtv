package com.vtv.appointment.service.strategy;

import com.vtv.appointment.model.dto.ScheduleQuery;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

public class ScheduleByMonthAndDayAndHour extends ScheduleFilter {
    public ScheduleByMonthAndDayAndHour(ScheduleQuery scheduleQuery) {
        super(scheduleQuery);
    }

    @Override
    public Boolean canHandle() {
        return Objects.nonNull(scheduleQuery.getMonth())
                && Objects.nonNull(scheduleQuery.getDayNumber())
                && Objects.nonNull(scheduleQuery.getHour());

    }

    @Override
    public Pair<ZonedDateTime, ZonedDateTime> find() {
        //TODO: Falta validaciones de negocio de hora
        final ZonedDateTime firstDateValid = ZonedDateTime.of(LocalDate.of(2023, scheduleQuery.getMonth(), scheduleQuery.getDayNumber()).atTime(scheduleQuery.getHour(), 0, 0), ZoneId.of("America/Buenos_Aires"));
        final ZonedDateTime lastDateValid = ZonedDateTime.of(LocalDate.of(2023, scheduleQuery.getMonth(), scheduleQuery.getDayNumber()).atTime(scheduleQuery.getHour(), 59, 59), ZoneId.of("America/Buenos_Aires"));
        return Pair.of(firstDateValid, lastDateValid);
    }
}
