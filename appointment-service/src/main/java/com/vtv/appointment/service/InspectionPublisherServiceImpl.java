package com.vtv.appointment.service;

import com.vtv.appointment.model.dto.OrderInspectionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InspectionPublisherServiceImpl implements InspectionPublisherService {

    private final RabbitTemplate rabbitTemplate;

    private final Queue queue;

    public InspectionPublisherServiceImpl(RabbitTemplate rabbitTemplate, Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    @Async //TODO: Deberia ser asincronico enviar el evento, o analizar si tiene sentido que pasa si falla digamos
    public void send(OrderInspectionDto message) {
        log.info("Send.. {}", message.toString());
        rabbitTemplate.convertAndSend(queue.getName(), message);
    }

    @Override
    public void orderInspection(OrderInspectionDto orderInspectionDto) {

    }
}
