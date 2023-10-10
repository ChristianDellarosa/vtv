package com.vtv.appointment.service;

import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.dto.AppointmentQuery;

import java.time.ZonedDateTime;
import java.util.List;

public interface AppointmentService {

    Appointment create(Appointment appointment);

    List<ZonedDateTime> getAvailable(AppointmentQuery appointmentQuery);
}
