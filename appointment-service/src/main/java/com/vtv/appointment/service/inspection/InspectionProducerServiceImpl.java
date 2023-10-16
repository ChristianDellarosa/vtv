package com.vtv.appointment.service.inspection;

import com.vtv.appointment.client.ProducerClient;
import com.vtv.appointment.model.dto.OrderInspectionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InspectionProducerServiceImpl implements InspectionProducerService {

    private final ProducerClient<OrderInspectionDto> producerClient;
    public InspectionProducerServiceImpl(ProducerClient<OrderInspectionDto> producerClient) {
        this.producerClient = producerClient;
    }

   //TODO: Deberia ser asincronico enviar el evento, o analizar si tiene sentido que pasa si falla digamos
    @Override
    public void orderInspection(OrderInspectionDto orderInspectionDto) {
        producerClient.send(orderInspectionDto);
    }
}
