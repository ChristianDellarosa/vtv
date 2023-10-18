package com.vtv.inspection.service.check;

import com.vtv.inspection.model.domain.CheckStepStatus;
import org.springframework.stereotype.Component;


@Component
public class FirstCheckStep extends CheckableStep {
    @Override
    public CheckableStepResult check() {
        return CheckableStepResult.builder()
                .score(10)
                .status(CheckStepStatus.SUCCESSFUL)
                .build();
    }
}
