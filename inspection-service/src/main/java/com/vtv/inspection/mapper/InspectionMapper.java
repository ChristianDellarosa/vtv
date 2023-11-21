package com.vtv.inspection.mapper;

import com.vtv.inspection.model.document.InspectionDocument;
import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.dto.InspectionDto;

public class InspectionMapper {

    public static Inspection toDomain(InspectionDocument inspectionDocument) {
        return Inspection.builder()
                .id(inspectionDocument.getId())
                .result(inspectionDocument.getResult())
                .score(inspectionDocument.getScore())
                .dateTime(inspectionDocument.getDateTime())
                .clientEmail(inspectionDocument.getClientEmail())
                .appointmentType(inspectionDocument.getAppointmentType())
                .carPlate(inspectionDocument.getCarPlate())
                .status(inspectionDocument.getStatus())
                .build();
    }

    public static InspectionDocument toEntity(Inspection inspection) {
        return InspectionDocument.builder()
                .id(inspection.getId())
                .clientEmail(inspection.getClientEmail())
                .appointmentType(inspection.getAppointmentType())
                .carPlate(inspection.getCarPlate())
                .status(inspection.getStatus())
                .result(inspection.getResult())
                .score(inspection.getScore())
                .dateTime(inspection.getDateTime())
                .build();
    }
}
