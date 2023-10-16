package com.vtv.appointment.service.schedule.filter;

import com.vtv.appointment.model.domain.ScheduleQuery;
import com.vtv.appointment.model.dto.ScheduleQueryDto;
import com.vtv.appointment.util.DateUtils;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Objects;

public class ScheduleWithoutFilter extends ScheduleFilter {
    public ScheduleWithoutFilter(ScheduleQuery scheduleQuery) {
        super(scheduleQuery);
    }

    @Override
    public Boolean canHandle() {
        return Objects.isNull(scheduleQuery.getMonth()) &&
                Objects.isNull(scheduleQuery.getDayNumber()) &&
                Objects.isNull(scheduleQuery.getHour());
    }

    @Override
    public Pair<ZonedDateTime, ZonedDateTime> find() {
        final ZonedDateTime firstDateValid = ZonedDateTime.of(LocalDate.of(DateUtils.getCurrentlyYear(), 1,1).with(TemporalAdjusters.firstDayOfMonth()).atTime(0, 0, 0), DateUtils.getZoneId());
        final ZonedDateTime lastDateValid = ZonedDateTime.of(LocalDate.of(DateUtils.getCurrentlyYear(), 12, 31).with(TemporalAdjusters.lastDayOfMonth()).atTime(23, 59, 59), DateUtils.getZoneId());
        return Pair.of(firstDateValid, lastDateValid);
    }
}
