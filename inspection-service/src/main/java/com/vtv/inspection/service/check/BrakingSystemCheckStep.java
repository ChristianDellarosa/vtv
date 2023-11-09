package com.vtv.inspection.service.check;

import com.vtv.inspection.configuration.CheckConfiguration;
import com.vtv.inspection.model.domain.CheckStepStatus;
import com.vtv.inspection.model.domain.CheckableStepName;
import com.vtv.inspection.model.domain.CheckableStepResult;
import org.springframework.stereotype.Component;

@Component
public class BrakingSystemCheckStep implements CheckableStep {
    private static final Integer SCORE = 10;

    private static final Integer FAIL_SCORE = -60;

    private static final Integer LESS_THAN_FIVE_SCORE = 4;

    private final CheckConfiguration checkConfiguration;

    public BrakingSystemCheckStep(CheckConfiguration checkConfiguration) {
        this.checkConfiguration = checkConfiguration;
    }

    @Override
    public CheckableStepResult check(String carPlate) {
        return CheckableStepResult.builder()
                .name(CheckableStepName.BRAKING_SYSTEM)
                .score(checkConfiguration.isFailCheckCase()? FAIL_SCORE : checkConfiguration.isLessThanFivePointsCheckCase()? LESS_THAN_FIVE_SCORE : SCORE)
                .status(CheckStepStatus.SUCCESSFUL)
                .build();
    }
}
