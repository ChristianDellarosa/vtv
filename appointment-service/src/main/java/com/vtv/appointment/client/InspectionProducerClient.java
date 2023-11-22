package com.vtv.appointment.client;

import com.vtv.appointment.exception.commons.GenericProducerException;
import com.vtv.appointment.model.domain.commons.ErrorDetail;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import com.vtv.appointment.model.dto.OrderInspectionDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InspectionProducerClient implements ProducerClient<OrderInspectionDto> {

    private final RabbitTemplate rabbitTemplate;

    private final Queue queue;

    public static final String INSPECTION_PRODUCER_ERROR_MESSAGE = "An error occurred while producing an inspection message";
    public static final Integer INSPECTION_PRODUCER_ERROR_CODE = 700;

    public InspectionProducerClient(RabbitTemplate rabbitTemplate, Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    @Override
    public void send(OrderInspectionDto message) {
        log.info("Order.. {}", message.toString());
        sendMessage(message);
    }

    private void sendMessage(OrderInspectionDto message) {
        try {
            rabbitTemplate.convertAndSend(queue.getName(), message);
        } catch (AmqpException amqpException) {
            log.error(INSPECTION_PRODUCER_ERROR_MESSAGE, amqpException);
            throw new GenericProducerException(
                    ExceptionError.builder()
                            .description(INSPECTION_PRODUCER_ERROR_MESSAGE)
                            .errorDetail(ErrorDetail.builder()
                                    .code(INSPECTION_PRODUCER_ERROR_CODE)
                                    .message(INSPECTION_PRODUCER_ERROR_MESSAGE)
                                    .build())
                            .build(), amqpException);
        }


    }
}
