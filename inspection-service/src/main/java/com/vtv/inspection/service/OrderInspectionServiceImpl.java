package com.vtv.inspection.service;

import com.vtv.inspection.exception.OrderInspectionStrategyNotExistsException;
import com.vtv.inspection.model.domain.InspectionOrder;
import com.vtv.inspection.model.domain.OrderType;
import com.vtv.inspection.model.domain.commons.ErrorDetail;
import com.vtv.inspection.model.domain.commons.ExceptionError;
import com.vtv.inspection.service.strategy.OrderInspectionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Service
@Slf4j
public class OrderInspectionServiceImpl implements OrderInspectionService {

    private final Map<OrderType, OrderInspectionStrategy> orderInspectionStrategyMap;

    public static final String ORDER_INSPECTION_STRATEGY_NOT_EXISTS_DESCRIPTION = "The strategy to process inspection order does not exist";
    public static final Integer ORDER_INSPECTION_STRATEGY_NOT_EXISTS_CODE = 300;
    public static final String ORDER_INSPECTION_STRATEGY_NOT_EXISTS_MESSAGE = "The order Inspection strategy not exists";

    public OrderInspectionServiceImpl(List<OrderInspectionStrategy> orderInspectionStrategies) {
        this.orderInspectionStrategyMap = orderInspectionStrategies.stream().collect(Collectors.toMap(OrderInspectionStrategy::getType, identity()));
    }

    @Override
    public void processOrder(InspectionOrder inspectionOrder) {
        Optional.ofNullable(orderInspectionStrategyMap.get(inspectionOrder.getOrderType()))
                .ifPresentOrElse(inspectionStrategy -> inspectionStrategy.execute(inspectionOrder),
                        () -> {
                            log.info(ORDER_INSPECTION_STRATEGY_NOT_EXISTS_DESCRIPTION);
                            throw new OrderInspectionStrategyNotExistsException(
                                    ExceptionError.builder()
                                            .description(ORDER_INSPECTION_STRATEGY_NOT_EXISTS_DESCRIPTION)
                                            .errorDetail(ErrorDetail.builder()
                                                    .code(ORDER_INSPECTION_STRATEGY_NOT_EXISTS_CODE)
                                                    .message(ORDER_INSPECTION_STRATEGY_NOT_EXISTS_MESSAGE)
                                                    .build())
                                            .build());
                        });

    }
}
