package com.vtv.inspection.service.check;

import com.vtv.inspection.model.domain.CheckStepStatus;
import com.vtv.inspection.model.domain.CheckableStepName;
import com.vtv.inspection.model.domain.CheckableStepResult;
import com.vtv.inspection.service.CheckStepScoreService;
import com.vtv.inspection.service.CheckStepStatusService;
import org.springframework.stereotype.Component;

import static com.vtv.inspection.model.domain.CheckStepStatus.isDangerousStatus;

@Component
public class FrontAxleCheckStep implements CheckableStep {
    private final CheckStepScoreService checkStepScoreService;

    private final CheckStepStatusService checkStepStatusService;

    public static final String WARNING_OBSERVATIONS = "Se necesita alineacion y balanceo";

    public static final String DANGEROUS_OBSERVATIONS = "Reparacion de tren delantero";

    public FrontAxleCheckStep(CheckStepScoreService checkStepScoreService,
                              CheckStepStatusService checkStepStatusService) {
        this.checkStepScoreService = checkStepScoreService;
        this.checkStepStatusService = checkStepStatusService;
    }

    @Override
    public CheckableStepResult check(String carPlate) {
        final var stepName = CheckableStepName.FRONT_AXLE;
        final var score = checkStepScoreService.getScore(stepName);
        final var status = checkStepStatusService.getStatus(score);

        return CheckableStepResult.builder()
                .name(stepName)
                .score(score)
                .status(status)
                .observations(isDangerousStatus(status) ? DANGEROUS_OBSERVATIONS : CheckStepStatus.isWarningStatus(status) ? WARNING_OBSERVATIONS : null)
                .build();
    }
}
