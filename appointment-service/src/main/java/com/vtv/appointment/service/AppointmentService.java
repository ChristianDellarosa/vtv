package com.vtv.appointment.service;

import com.vtv.appointment.model.domain.Appointment;

public interface AppointmentService {

    Appointment create(Appointment appointment);

    Appointment getLastByCarPlate(String id);
}
