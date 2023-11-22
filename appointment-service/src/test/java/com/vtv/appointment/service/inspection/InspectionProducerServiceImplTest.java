package com.vtv.appointment.service.inspection;

import com.vtv.appointment.client.ProducerClient;
import com.vtv.appointment.exception.OrderInspectionErrorException;
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

import static com.vtv.appointment.service.inspection.InspectionProducerServiceImpl.*;
import static com.vtv.appointment.service.inspection.InspectionProducerServiceImpl.INSPECTION_PRODUCER_ERROR_CODE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class InspectionProducerServiceImplTest {

    @Mock
    private ProducerClient<OrderInspectionDto> producerClient;
    @InjectMocks
    private InspectionProducerServiceImpl inspectionProducerService;

    @Test
    public void orderInspection_successfully() {
        final OrderInspectionDto orderInspectionDto = OrderInspectionDto.builder()
                .orderType(OrderType.CREATE)
                .build();

        inspectionProducerService.orderInspection(orderInspectionDto);
    }

    @Test
    public void orderInspection_hasError_throw_OrderInspectionErrorException() {
        final OrderInspectionDto orderInspectionDto = OrderInspectionDto.builder().orderType(OrderType.CREATE).build();

        final var exceptionExpected = new OrderInspectionErrorException(
                ExceptionError.builder()
                        .description(INSPECTION_PRODUCER_ERROR_MESSAGE)
                        .errorDetail(ErrorDetail.builder()
                                .code(INSPECTION_PRODUCER_ERROR_CODE)
                                .message(INSPECTION_PRODUCER_ERROR_MESSAGE)
                                .build())
                        .build(), null);

        doThrow(GenericProducerException.class).when(producerClient).send(orderInspectionDto);

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () ->  inspectionProducerService.orderInspection(orderInspectionDto));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(producerClient).send(orderInspectionDto);
    }
}
