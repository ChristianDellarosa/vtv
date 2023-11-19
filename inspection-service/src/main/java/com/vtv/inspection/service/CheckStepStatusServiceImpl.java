package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.CheckStepStatus;
import org.springframework.stereotype.Service;

@Service
public class CheckStepStatusServiceImpl implements CheckStepStatusService {

    public static final Integer SUCCESSFULLY_STATUS = 10;

    public static final Integer WARNING_STATUS = 4;
    @Override
    public CheckStepStatus getStatus(Integer score) {
        return SUCCESSFULLY_STATUS.equals(score) ? CheckStepStatus.SUCCESSFUL : WARNING_STATUS.compareTo(score) >= 0 ? CheckStepStatus.DANGEROUS : CheckStepStatus.WARNING;
    }
}
