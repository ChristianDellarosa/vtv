package com.vtv.inspection.service.check;

import com.vtv.inspection.model.domain.CheckStepStatus;
import org.springframework.stereotype.Component;


@Component
public class FirstCheckStep extends CheckableStep {
    @Override
    public CheckableStepResult check(String carPlate) {
        return CheckableStepResult.builder()
                .name("FirstCheckStep") //TODO: Este this.getName() funciona si generamos el FirstcheckStep en un bean
                .score(100)
                .status(CheckStepStatus.SUCCESSFUL)
                .build();
    }
}
