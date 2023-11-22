package com.vtv.appointment.service.schedule;

import com.vtv.appointment.model.domain.ScheduleQuery;
import com.vtv.appointment.model.dto.ScheduleQueryDto;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface ScheduleService {
    List<ZonedDateTime> get(ScheduleQuery scheduleQuery);

    Boolean isValidDate(ZonedDateTime dateTime);

    Boolean isAvailableDateTime(ZonedDateTime dateTime);

    Boolean isValidTime(LocalTime dateTime);
}
