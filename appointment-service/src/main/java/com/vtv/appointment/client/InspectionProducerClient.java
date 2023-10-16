package com.vtv.appointment.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vtv.appointment.model.dto.OrderInspectionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InspectionProducerClient implements ProducerClient<OrderInspectionDto> {

    private final RabbitTemplate rabbitTemplate;

    private final Queue queue;

    private final ObjectMapper objectMapper;

    public InspectionProducerClient(RabbitTemplate rabbitTemplate, Queue queue, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
        this.objectMapper = objectMapper;
    }

    @Override
    public void send(OrderInspectionDto message) {
        log.info("Order.. {}", message.toString());
        rabbitTemplate.convertAndSend(queue.getName(), message);
    }
}
