package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.CheckStepStatus;
import org.springframework.stereotype.Service;

@Service
public class CheckStepStatusServiceImpl implements CheckStepStatusService {

    private static final Integer SUCCESSFULLY_SCORE = 10;

    private static final Integer WARNING_SCORE = 4;
    @Override
    public CheckStepStatus getStatus(Integer score) {
        return SUCCESSFULLY_SCORE.equals(score) ? CheckStepStatus.SUCCESSFUL : WARNING_SCORE.compareTo(score) <= 0 ? CheckStepStatus.WARNING : CheckStepStatus.DANGEROUS;
    }
}
