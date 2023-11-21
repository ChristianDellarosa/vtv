package com.vtv.inspection.service.strategy;

import com.vtv.inspection.mock.InspectionFactory;
import com.vtv.inspection.mock.InspectionOrderFactory;
import com.vtv.inspection.model.domain.OrderType;
import com.vtv.inspection.repository.InspectionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderCreateInspectionStrategyTest {
    @Mock
    private InspectionRepository inspectionRepository;

    @InjectMocks
    private OrderCreateInspectionStrategy orderInspectionStrategy;


    @Test
    void shouldExecuteSuccessfully() {
        final var inspectionOrder = InspectionOrderFactory.buildInspectionOrder();

        final var inspection = InspectionFactory.buildInspection();

        when(inspectionRepository.save(inspection)).thenReturn(inspection);

        orderInspectionStrategy.execute(inspectionOrder);

        verify(inspectionRepository).save(inspection);
    }

    @Test
    void shouldGetTypSuccessfully() {
        final var orderTypeExpected = OrderType.CREATE;
        final OrderType orderType = orderInspectionStrategy.getType();

        assertEquals(orderTypeExpected, orderType);
    }
}
