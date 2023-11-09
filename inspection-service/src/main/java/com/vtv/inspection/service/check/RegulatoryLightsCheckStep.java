package com.vtv.inspection.service.check;

import com.vtv.inspection.model.domain.CheckStepStatus;
import com.vtv.inspection.model.domain.CheckableStepName;
import com.vtv.inspection.model.domain.CheckableStepResult;
import org.springframework.stereotype.Component;

@Component
public class RegulatoryLightsCheckStep implements CheckableStep {
    private static final Integer SCORE = 10;

    @Override
    public CheckableStepResult check(String carPlate) { //TODO: Ver de hacer algo randomizado para simular el ejemplo

        return CheckableStepResult.builder()
                .name(CheckableStepName.REGULATORY_LIGHTS)
                .score(SCORE)
                .status(CheckStepStatus.SUCCESSFUL)
                .build();
    }
}
