package com.vtv.appointment.service.strategy;

import com.vtv.appointment.model.dto.AppointmentQuery;
import lombok.Getter;
import org.springframework.data.util.Pair;

import java.time.ZonedDateTime;


public abstract class AppointmentFilter {
    @Getter
    protected final AppointmentQuery appointmentQuery;
    public AppointmentFilter(AppointmentQuery appointmentQuery) {
        this.appointmentQuery = appointmentQuery;
    }
    public abstract Boolean canHandle();

    public abstract Pair<ZonedDateTime, ZonedDateTime> find();
}
