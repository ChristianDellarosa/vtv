package com.vtv.inspection.service;

import com.vtv.inspection.exception.OrderInspectionStrategyNotExistsException;
import com.vtv.inspection.model.domain.InspectionOrder;
import com.vtv.inspection.model.domain.OrderType;
import com.vtv.inspection.model.domain.commons.ErrorDetail;
import com.vtv.inspection.model.domain.commons.ExceptionError;
import com.vtv.inspection.service.strategy.OrderCreateInspectionStrategy;
import com.vtv.inspection.service.strategy.OrderInspectionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static com.vtv.inspection.service.OrderInspectionServiceImpl.ORDER_INSPECTION_STRATEGY_NOT_EXISTS_CODE;
import static com.vtv.inspection.service.OrderInspectionServiceImpl.ORDER_INSPECTION_STRATEGY_NOT_EXISTS_DESCRIPTION;
import static com.vtv.inspection.service.OrderInspectionServiceImpl.ORDER_INSPECTION_STRATEGY_NOT_EXISTS_MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderInspectionServiceImplTest {

    @Mock
    private OrderCreateInspectionStrategy orderCreateInspectionStrategy;
    private OrderInspectionServiceImpl orderInspectionService;

    @BeforeEach
    void setUp() {
        when(orderCreateInspectionStrategy.getType())
                .thenReturn(OrderType.CREATE);

        orderInspectionService = new OrderInspectionServiceImpl(Collections.singletonList(orderCreateInspectionStrategy));
    }

    @Test
    void shouldProcessOrderWhenOrderInspectionStrategyExists() {
        final InspectionOrder inspectionOrder = InspectionOrder.builder()
                .orderType(OrderType.CREATE)
                .build();

        orderInspectionService.processOrder(inspectionOrder);

        verify(orderCreateInspectionStrategy).execute(inspectionOrder);
        verify(orderCreateInspectionStrategy).getType();
    }

    @Test
    void WhenOrderInspectionStrategyDoesNotExist_ShouldThrowOrderInspectionStrategyNotExistsException() {
        final InspectionOrder inspectionOrder = InspectionOrder.builder()
                .orderType(OrderType.UPDATE)
                .build();

        final var exceptionExpected = new OrderInspectionStrategyNotExistsException(
                ExceptionError.builder()
                        .description(ORDER_INSPECTION_STRATEGY_NOT_EXISTS_DESCRIPTION)
                        .errorDetail(ErrorDetail.builder()
                                .code(ORDER_INSPECTION_STRATEGY_NOT_EXISTS_CODE)
                                .message(ORDER_INSPECTION_STRATEGY_NOT_EXISTS_MESSAGE)
                                .build())
                        .build());

        final var exceptionThrown = assertThrows(exceptionExpected.getClass(),
                () ->  orderInspectionService.processOrder(inspectionOrder));

        assertEquals(exceptionThrown.getMessage(), exceptionExpected.getMessage());
        assertEquals(exceptionThrown.getExceptionError().description(), exceptionExpected.getExceptionError().description());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().message(), exceptionExpected.getExceptionError().errorDetail().message());
        assertEquals(exceptionThrown.getExceptionError().errorDetail().code(), exceptionExpected.getExceptionError().errorDetail().code());

        verify(orderCreateInspectionStrategy).getType();
    }
}