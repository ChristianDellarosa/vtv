package com.vtv.inspection.mapper;

import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.dto.InspectionDto;

public class InspectionDtoMapper {
    public static InspectionDto toDto(Inspection inspection) {
        return InspectionDto.builder()
                .id(inspection.getId())
                .carPlate(inspection.getCarPlate())
                .result(inspection.getResult())
                .score(inspection.getScore())
                .appointmentType(inspection.getAppointmentType())
                .status(inspection.getStatus())
                .dateTime(inspection.getDateTime())
                .clientEmail(inspection.getClientEmail())
                .build();
    }
}
