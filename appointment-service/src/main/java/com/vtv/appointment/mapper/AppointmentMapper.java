package com.vtv.appointment.mapper;

import com.vtv.appointment.model.document.AppointmentDocument;
import com.vtv.appointment.model.domain.Appointment;

public class AppointmentMapper {

    public static AppointmentDocument toEntity(Appointment appointment) {
        return AppointmentDocument.builder()
                .clientEmail(appointment.getClientEmail())
                .type(appointment.getType())
                .carPlate(appointment.getCarPlate())
                .dateTime(appointment.getDateTime())
                .id(appointment.getId())
                .build();
    }

    public static Appointment toDomain(AppointmentDocument appointmentDocument) {
        return Appointment.builder()
                .id(appointmentDocument.getId())
                .clientEmail(appointmentDocument.getClientEmail())
                .type(appointmentDocument.getType())
                .carPlate(appointmentDocument.getCarPlate())
                .dateTime(appointmentDocument.getDateTime())
                .build();
    }
}
