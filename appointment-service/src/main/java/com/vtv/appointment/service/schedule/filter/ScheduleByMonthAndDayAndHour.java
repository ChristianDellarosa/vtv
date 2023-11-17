package com.vtv.appointment.service.schedule.filter;

import com.vtv.appointment.model.domain.ScheduleQuery;
import com.vtv.appointment.model.dto.ScheduleQueryDto;
import com.vtv.appointment.util.DateUtils;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.vtv.appointment.util.DateUtils.*;

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
        final ZonedDateTime firstDateValid = ZonedDateTime.of(LocalDate.of(getCurrentlyYear(), scheduleQuery.getMonth(), scheduleQuery.getDayNumber()).atTime(scheduleQuery.getHour(), MINUTE_ZERO, SECOND_ZERO), DateUtils.getZoneId());
        final ZonedDateTime lastDateValid = ZonedDateTime.of(LocalDate.of(getCurrentlyYear(), scheduleQuery.getMonth(), scheduleQuery.getDayNumber()).atTime(scheduleQuery.getHour(), MINUTE_FIFTY_NINE, SECOND_FIFTY_NINE), DateUtils.getZoneId());
        return Pair.of(firstDateValid, lastDateValid);
    }
}
