package com.vtv.inspection.service.strategy;

import com.vtv.inspection.model.domain.InspectionOrder;
import com.vtv.inspection.model.domain.OrderType;

public interface OrderInspectionStrategy {

    void execute (InspectionOrder inspectionOrder);
    //TODO: La estrategia del save y la del update

    OrderType getType();
}
