package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.InspectionOrder;

public interface OrderInspectionService {
    void processOrder(InspectionOrder inspectionOrder);
}
