package com.vtv.appointment.repository;

import com.vtv.appointment.model.document.AppointmentDocument;
import com.vtv.appointment.model.domain.Appointment;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public interface AppointmentRepository {

    AppointmentDocument create(Appointment appointment);

    List<Appointment> getByCarPlate(String carPlate);

    List<Appointment> getByDateTimeRange(ZonedDateTime from, ZonedDateTime to);
}
