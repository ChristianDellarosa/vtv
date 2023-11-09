package com.vtv.inspection.consumer;


import com.vtv.inspection.model.domain.InspectionOrder;
import com.vtv.inspection.model.dto.InspectionOrderDto;
import com.vtv.inspection.service.OrderInspectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderInspectionConsumer {

    private final OrderInspectionService orderInspectionService;

    public OrderInspectionConsumer(OrderInspectionService orderInspectionService) {
        this.orderInspectionService = orderInspectionService;
    }

    @RabbitListener(queues = "${queues.appointment}")
    public void receive(@Payload InspectionOrderDto message) { //TODO: Analizar si el OrderType puede viajar por header
        log.info("Process Order {}", message.toString());
        orderInspectionService.processOrder(
                InspectionOrder.builder()
                        .appointmentType(message.getAppointmentType())
                        .clientEmail(message.getClientEmail())
                        .dateTime(message.getDateTime())
                        .carPlate(message.getCarPlate())
                        .orderType(message.getOrderType())
                        .build());
    }
}