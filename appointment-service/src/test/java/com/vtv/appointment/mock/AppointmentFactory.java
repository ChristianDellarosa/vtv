package com.vtv.appointment.mock;

import com.vtv.appointment.model.document.AppointmentDocument;
import com.vtv.appointment.model.domain.Appointment;
import com.vtv.appointment.model.domain.AppointmentType;

import java.time.ZonedDateTime;

public class AppointmentFactory {

    private final static ZonedDateTime NOW = ZonedDateTime.now();

    public static Appointment buildAppointment() {
        return Appointment.builder()
                .type(AppointmentType.INSPECTION)
                .carPlate("KKK000")
                .clientEmail("anEmail@gmail.com")
                .dateTime(NOW)
                .build();
    }

    public static Appointment buildAppointmentAfterSave() {
        return Appointment.builder()
                .id("anId")
                .type(AppointmentType.INSPECTION)
                .carPlate("KKK000")
                .clientEmail("anEmail@gmail.com")
                .dateTime(NOW)
                .build();
    }
    public static AppointmentDocument buildAppointmentDocument() {
        return AppointmentDocument.builder()
                .type(AppointmentType.INSPECTION)
                .carPlate("KKK000")
                .clientEmail("anEmail@gmail.com")
                .dateTime(NOW)
                .build();
    }

    public static AppointmentDocument buildAppointmentDocumentAfterSave() {
        return AppointmentDocument.builder()
                .id("anId")
                .type(AppointmentType.INSPECTION)
                .carPlate("KKK000")
                .clientEmail("anEmail@gmail.com")
                .dateTime(NOW)
                .build();
    }
}
