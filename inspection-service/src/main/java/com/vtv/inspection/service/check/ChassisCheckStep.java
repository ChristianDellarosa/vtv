package com.vtv.inspection.service.check;

import com.vtv.inspection.model.domain.CheckStepStatus;
import com.vtv.inspection.model.domain.CheckableStepName;
import com.vtv.inspection.model.domain.CheckableStepResult;
import com.vtv.inspection.service.CheckStepScoreService;
import com.vtv.inspection.service.CheckStepStatusService;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import static com.vtv.inspection.model.domain.CheckStepStatus.isDangerousStatus;

@Component
public class ChassisCheckStep implements CheckableStep {
    private final CheckStepScoreService checkStepScoreService;

    private final CheckStepStatusService checkStepStatusService;

    public static final String WARNING_OBSERVATIONS = "Deberia realizarse el arreglo del paragolpes trasero";

    public static final String DANGEROUS_OBSERVATIONS = "Reparacion urgente del capot";

    public ChassisCheckStep(CheckStepScoreService checkStepScoreService, CheckStepStatusService checkStepStatusService) {
        this.checkStepScoreService = checkStepScoreService;
        this.checkStepStatusService = checkStepStatusService;
    }

    @Override
    public CheckableStepResult check(String carPlate) {
        final var stepName = CheckableStepName.CHASSIS;
        final var score = checkStepScoreService.getScore(stepName);
        final var status = checkStepStatusService.getStatus(score);

        return CheckableStepResult.builder()
                .name(stepName)
                .score(score)
                .status(status)
                .observations(isDangerousStatus(status) ? DANGEROUS_OBSERVATIONS : CheckStepStatus.isWarningStatus(status) ? WARNING_OBSERVATIONS : null)
                .dateTime(ZonedDateTime.now())
                .build();
    }
}
