package com.vtv.appointment.service.schedule.filter;

import com.vtv.appointment.model.domain.ScheduleQuery;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

import static com.vtv.appointment.util.DateUtils.*;
import static com.vtv.appointment.util.DateUtils.FIRST_DAY_OF_MONTH;
import static com.vtv.appointment.util.DateUtils.getCurrentlyYear;

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
        final ZonedDateTime firstDateValid = ZonedDateTime.of(LocalDate.of(getCurrentlyYear(), scheduleQuery.getMonth(), FIRST_DAY_OF_MONTH).with(TemporalAdjusters.firstDayOfMonth()).atTime(HOUR_ZERO, MINUTE_ZERO, SECOND_ZERO), getZoneId());
        final ZonedDateTime lastDateValid = ZonedDateTime.of(LocalDate.of(getCurrentlyYear(), scheduleQuery.getMonth(), LAST_DAY_OF_MONTH).with(TemporalAdjusters.lastDayOfMonth()).atTime(HOUR_TWENTY_TREE, MINUTE_FIFTY_NINE, SECOND_FIFTY_NINE), getZoneId());
        return Pair.of(firstDateValid, lastDateValid);
    }
}
