package com.vtv.inspection.model.dto;

import com.vtv.inspection.model.domain.CheckableStepResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InspectionResultDto {
    private List<CheckableStepResultDto> checkableStepResults;
}
