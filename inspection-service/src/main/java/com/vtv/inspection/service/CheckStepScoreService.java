package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.CheckableStepName;

public interface CheckStepScoreService {

    Integer getScore(CheckableStepName stepName);
}
