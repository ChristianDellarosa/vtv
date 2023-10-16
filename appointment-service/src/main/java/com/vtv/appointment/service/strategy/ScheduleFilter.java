package com.vtv.appointment.service.strategy;

import com.vtv.appointment.model.dto.ScheduleQuery;
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
