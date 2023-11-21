package com.vtv.inspection.mapper;

import com.vtv.inspection.model.domain.InspectionRequest;
import com.vtv.inspection.model.dto.InspectionRequestDto;

public class InspectionRequestDtoMapper {
    public static InspectionRequest toDomain(InspectionRequestDto inspectionRequestDto) {
        return InspectionRequest.builder()
                .carPlate(inspectionRequestDto.getCarPlate())
                .type(inspectionRequestDto.getType())
                .build();
    }
}
