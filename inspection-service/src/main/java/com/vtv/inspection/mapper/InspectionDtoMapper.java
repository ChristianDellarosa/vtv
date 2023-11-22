package com.vtv.inspection.mapper;

import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.dto.InspectionDto;
import com.vtv.inspection.model.dto.InspectionResultDto;

public class InspectionDtoMapper {
    public static InspectionDto toDto(Inspection inspection) {
        return InspectionDto.builder()
                .id(inspection.getId())
                .carPlate(inspection.getCarPlate())
                .result(InspectionResultMapper.toDto(inspection.getResult()))
                .score(inspection.getScore())
                .appointmentType(inspection.getAppointmentType())
                .status(inspection.getStatus())
                .dateTime(inspection.getDateTime())
                .clientEmail(inspection.getClientEmail())
                .build();
    }
}
