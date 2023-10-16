package com.vtv.appointment.service.strategy;

import com.vtv.appointment.model.dto.ScheduleQuery;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

public class ScheduleByMonthFilter extends ScheduleFilter {
    public ScheduleByMonthFilter(ScheduleQuery scheduleQuery) {
        super(scheduleQuery);
    }

    @Override
    public Boolean canHandle() {
        return Objects.nonNull(scheduleQuery.getMonth()) &&
                Objects.isNull(scheduleQuery.getDayNumber()) &&
                Objects.isNull(scheduleQuery.getHour());

    }

    @Override
    public Pair<ZonedDateTime, ZonedDateTime> find() {
        final ZonedDateTime firstDateValid = ZonedDateTime.of(LocalDate.of(2023, scheduleQuery.getMonth(), 1).with(TemporalAdjusters.firstDayOfMonth()).atTime(0, 0, 0), ZoneId.of("America/Buenos_Aires"));
        final ZonedDateTime lastDateValid = ZonedDateTime.of(LocalDate.of(2023, scheduleQuery.getMonth(), 31).with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59), ZoneId.of("America/Buenos_Aires"));
        return Pair.of(firstDateValid, lastDateValid);
    }
}
