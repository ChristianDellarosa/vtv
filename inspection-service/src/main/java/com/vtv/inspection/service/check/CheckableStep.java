package com.vtv.inspection.service.check;

import com.vtv.inspection.model.domain.CheckableStepResult;

public interface CheckableStep {
   CheckableStepResult check(String carPlate);
}
