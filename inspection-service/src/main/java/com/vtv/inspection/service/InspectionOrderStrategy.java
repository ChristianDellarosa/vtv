package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.InspectionOrder;

public interface InspectionOrderStrategy {

    void execute (InspectionOrder inspectionOrder);
    //TODO: La estrategia del save y la del update
}
