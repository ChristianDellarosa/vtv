package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.CheckStepStatus;

public interface CheckStepStatusService {

    CheckStepStatus getStatus(Integer score);
}
