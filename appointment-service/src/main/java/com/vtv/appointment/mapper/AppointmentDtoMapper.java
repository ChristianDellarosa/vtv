package com.vtv.appointment.mapper;

import com.vtv.appointment.model.document.AppointmentDocument;
import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.dto.AppointmentDto;
import com.vtv.appointment.util.DateUtils;

import java.time.ZonedDateTime;

public class AppointmentDtoMapper {
    public static AppointmentDto toDto(Appointment appointment) {
        return AppointmentDto.builder()
                .carPlate(appointment.getCarPlate())
                .type(appointment.getType())
                .clientEmail(appointment.getClientEmail())
                .dateTime(appointment.getDateTime().toLocalDateTime())
                .build();
    }

    public static Appointment toDomain(AppointmentDto appointmentDto) {
        return Appointment.builder()
                .carPlate(appointmentDto.getCarPlate())
                .clientEmail(appointmentDto.getClientEmail())
                .dateTime(ZonedDateTime.of(appointmentDto.getDateTime(), DateUtils.getZoneId()))
                .type(appointmentDto.getType())
                .build();
    }
}
