package com.vtv.appointment.service.schedule.filter;

import com.vtv.appointment.model.domain.ScheduleQuery;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.vtv.appointment.util.DateUtils.*;

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
        final ZonedDateTime firstDateValid = ZonedDateTime.of(LocalDate.of(getCurrentlyYear(), scheduleQuery.getMonth(), scheduleQuery.getDayNumber()).atTime(HOUR_ZERO, MINUTE_ZERO, SECOND_ZERO), getZoneId());
        final ZonedDateTime lastDateValid = ZonedDateTime.of(LocalDate.of(getCurrentlyYear(), scheduleQuery.getMonth(), scheduleQuery.getDayNumber()).atTime(HOUR_TWENTY_TREE, MINUTE_FIFTY_NINE, SECOND_FIFTY_NINE), getZoneId());
        return Pair.of(firstDateValid, lastDateValid);
    }

}
