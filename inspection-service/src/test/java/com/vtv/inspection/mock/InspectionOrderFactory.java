package com.vtv.inspection.mock;

import com.vtv.inspection.model.domain.AppointmentType;
import com.vtv.inspection.model.domain.InspectionOrder;
import com.vtv.inspection.model.domain.OrderType;

public class InspectionOrderFactory {
    public static InspectionOrder buildInspectionOrder() {
        return InspectionOrder.builder()
                .appointmentType(AppointmentType.INSPECTION)
                .carPlate("ABC123")
                .orderType(OrderType.CREATE)
                .build();
    }
}
