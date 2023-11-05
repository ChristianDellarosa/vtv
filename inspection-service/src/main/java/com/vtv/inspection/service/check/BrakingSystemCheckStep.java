package com.vtv.inspection.service.check;

import com.vtv.inspection.model.domain.CheckStepStatus;
import com.vtv.inspection.model.domain.CheckableStepName;
import com.vtv.inspection.model.domain.CheckableStepResult;
import org.springframework.stereotype.Component;

@Component
public class BrakingSystemCheckStep implements CheckableStep {
    private static final Integer SCORE = 10;

    @Override
    public CheckableStepResult check(String carPlate) {
        return CheckableStepResult.builder()
                .name(CheckableStepName.BRAKING_SYSTEM)
                .score(SCORE)
                .status(CheckStepStatus.SUCCESSFUL)
                .build();
    }
}
