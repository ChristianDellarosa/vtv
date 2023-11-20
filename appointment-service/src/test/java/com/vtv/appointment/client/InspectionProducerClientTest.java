package com.vtv.appointment.client;

import com.vtv.appointment.exception.commons.GenericProducerException;
import com.vtv.appointment.model.domain.commons.ErrorDetail;
import com.vtv.appointment.model.domain.commons.ExceptionError;
import com.vtv.appointment.model.dto.OrderInspectionDto;
import com.vtv.appointment.model.dto.OrderType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static com.vtv.appointment.client.InspectionProducerClient.INSPECTION_PRODUCER_ERROR_CODE;
import static com.vtv.appointment.client.InspectionProducerClient.INSPECTION_PRODUCER_ERROR_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InspectionProducerClientTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private Queue queue;

    @InjectMocks
    private InspectionProducerClient inspectionProducerClient;

    @Test
    public void send_successfully() {
        final OrderInspectionDto orderInspectionDto = OrderInspectionDto.builder().orderType(OrderType.CREATE).build();
        inspectionProducerClient.send(orderInspectionDto);
    }

    @Test
    public void send_WithAmqpException_throw_GenericProducerException() {
        final var nameQueue = "aQueueName";
        final OrderInspectionDto orderInspectionDto = OrderInspectionDto.builder().orderType(OrderType.CREATE).build();

        final var exceptionExpected = new GenericProducerException(
                ExceptionError.builder()
                        .description(INSPECTION_PRODUCER_ERROR_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .message(INSPECTION_PRODUCER_ERROR_MESSAGE)
                                .code(INSPECTION_PRODUCER_ERROR_CODE)
                                .build())
                        .build(), null);

        when(queue.getName()).thenReturn("aQueueName");

        doThrow(AmqpException.class)
                .when(rabbitTemplate).convertAndSend(nameQueue, orderInspectionDto);

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () ->  inspectionProducerClient.send(orderInspectionDto));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(queue).getName();
    }
}
