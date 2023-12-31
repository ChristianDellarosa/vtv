package com.vtv.inspection.mock;

import com.vtv.inspection.mapper.InspectionMapper;
import com.vtv.inspection.model.document.InspectionDocument;
import com.vtv.inspection.model.domain.AppointmentType;
import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.domain.InspectionStatus;

import java.time.ZonedDateTime;

public class InspectionFactory {

    public static Inspection buildInspection() {
        return Inspection.builder()
                .appointmentType(AppointmentType.INSPECTION)
                .carPlate("ABC123")
                .status(InspectionStatus.PENDING)
                .build();
    }

    public static Inspection buildInspection(ZonedDateTime dateTime) {
        return Inspection.builder()
                .appointmentType(AppointmentType.INSPECTION)
                .carPlate("ABC123")
                .status(InspectionStatus.PENDING)
                .dateTime(dateTime)
                .build();
    }

    public static InspectionDocument buildInspectionDocument() {
        return InspectionDocument.builder()
                .appointmentType(AppointmentType.INSPECTION)
                .carPlate("ABC123")
                .status(InspectionStatus.PENDING)
                .build();
    }
}
