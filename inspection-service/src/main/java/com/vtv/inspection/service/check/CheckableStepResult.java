package com.vtv.inspection.service.check;

import com.vtv.inspection.model.domain.CheckStepStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckableStepResult {
    private String name;
    private String observations; //TODO: Ver si estos campos no son del resultado
    private Integer score; //TODO: Ver si estos campos no son del resultado
    private CheckStepStatus status; //TODO: Ver si estos campos no son del resultado
}
