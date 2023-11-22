package com.vtv.appointment.service.schedule.filter;

import com.vtv.appointment.exception.ScheduleFilterException;
import com.vtv.appointment.model.domain.ScheduleQuery;
import com.vtv.appointment.model.domain.commons.ErrorDetail;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import com.vtv.appointment.model.dto.ScheduleQueryDto;
import com.vtv.appointment.util.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static com.vtv.appointment.util.DateUtils.*;

@Slf4j
public class ScheduleByMonthAndDayAndHour extends ScheduleFilter {

    public static final String FILTER_COMBINATION_INVALID_MESSAGE = "The request filter combination is invalid.";
    public static final Integer FILTER_COMBINATION_INVALID_CODE = 310;

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
        try {
            final ZonedDateTime firstDateValid = ZonedDateTime.of(LocalDate.of(getCurrentlyYear(), scheduleQuery.getMonth(), scheduleQuery.getDayNumber()).atTime(scheduleQuery.getHour(), MINUTE_ZERO, SECOND_ZERO), DateUtils.getZoneId());
            final ZonedDateTime lastDateValid = ZonedDateTime.of(LocalDate.of(getCurrentlyYear(), scheduleQuery.getMonth(), scheduleQuery.getDayNumber()).atTime(scheduleQuery.getHour(), MINUTE_FIFTY_NINE, SECOND_FIFTY_NINE), DateUtils.getZoneId());
            return Pair.of(firstDateValid, lastDateValid);
        } catch (DateTimeException dateTimeException) {
            log.error(FILTER_COMBINATION_INVALID_MESSAGE, dateTimeException);
            throw new ScheduleFilterException(
                    ExceptionError.builder()
                            .description(FILTER_COMBINATION_INVALID_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(FILTER_COMBINATION_INVALID_CODE)
                                    .message(FILTER_COMBINATION_INVALID_MESSAGE)
                                    .build())
                            .build());
        }
    }
}
