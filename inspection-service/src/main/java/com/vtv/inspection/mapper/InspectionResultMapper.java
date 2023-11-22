package com.vtv.inspection.mapper;

import com.vtv.inspection.model.domain.CheckableStepResult;
import com.vtv.inspection.model.domain.InspectionResult;
import com.vtv.inspection.model.dto.CheckableStepResultDto;
import com.vtv.inspection.model.dto.InspectionResultDto;

import java.util.Objects;
import java.util.stream.Collectors;

public class InspectionResultMapper {
    public static InspectionResultDto toDto(InspectionResult inspectionResult) {
        return Objects.nonNull(inspectionResult)? InspectionResultDto.builder()
                .checkableStepResults(
                        inspectionResult.getCheckableStepResults()
                                .stream()
                                .map(InspectionResultMapper::toDto)
                                .collect(Collectors.toList()))
                .build() : null;
    }

    private static CheckableStepResultDto toDto(CheckableStepResult checkableStepResult) {
        return CheckableStepResultDto.builder()
                .name(checkableStepResult.getName())
                .score(checkableStepResult.getScore())
                .observations(checkableStepResult.getObservations())
                .dateTime(checkableStepResult.getDateTime())
                .build();
    }
}
