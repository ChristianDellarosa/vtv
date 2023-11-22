package com.vtv.appointment.service.schedule.filter;

import com.vtv.appointment.model.domain.ScheduleQuery;
import lombok.Getter;
import org.springframework.data.util.Pair;

import java.time.ZonedDateTime;


public abstract class ScheduleFilter {
    @Getter
    protected final ScheduleQuery scheduleQuery;
    public ScheduleFilter(ScheduleQuery scheduleQuery) {
        this.scheduleQuery = scheduleQuery;
    }
    public abstract Boolean canHandle();

    public abstract Pair<ZonedDateTime, ZonedDateTime> find();
}
