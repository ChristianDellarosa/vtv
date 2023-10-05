package com.vtv.appointment.service;

import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.dto.DateTimeFilter;

import java.util.List;

public interface AppointmentService {

    Appointment create(Appointment appointment);

    List<Appointment> getAvailable(DateTimeFilter dateTimeFilter);
}
