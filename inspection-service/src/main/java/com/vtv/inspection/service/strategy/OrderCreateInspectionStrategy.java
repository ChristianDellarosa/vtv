package com.vtv.inspection.service.strategy;

import com.vtv.inspection.model.domain.Inspection;
import com.vtv.inspection.model.domain.InspectionOrder;
import com.vtv.inspection.model.domain.InspectionStatus;
import com.vtv.inspection.model.domain.OrderType;
import com.vtv.inspection.repository.InspectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderCreateInspectionStrategy implements OrderInspectionStrategy {

    private final InspectionRepository inspectionRepository;

    public OrderCreateInspectionStrategy(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    @Override
    public void execute(InspectionOrder inspectionOrder) {
        inspectionRepository.save(
                Inspection.builder()
                .appointmentType(inspectionOrder.getAppointmentType())
                .status(InspectionStatus.PENDING)
                .dateTime(inspectionOrder.getDateTime())
                .carPlate(inspectionOrder.getCarPlate())
                .clientEmail(inspectionOrder.getClientEmail())
                .build()
        );
    }

    @Override
    public OrderType getType() {
        return OrderType.CREATE;
    }
}
