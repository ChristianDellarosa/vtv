package com.vtv.inspection.service;

import com.vtv.inspection.model.domain.InspectionOrder;
import com.vtv.inspection.model.domain.OrderType;
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

    public OrderInspectionServiceImpl(List<OrderInspectionStrategy> orderInspectionStrategies) {
        this.orderInspectionStrategyMap = orderInspectionStrategies.stream().collect(Collectors.toMap(OrderInspectionStrategy::getType, identity()));
    }

    @Override
    public void processOrder(InspectionOrder inspectionOrder) {
        Optional.ofNullable(orderInspectionStrategyMap.get(inspectionOrder.getOrderType()))
                .ifPresentOrElse(inspectionStrategy -> inspectionStrategy.execute(inspectionOrder),
                        () -> {
                            log.info("NOT FOUND STRATEGY");
                            throw new IllegalArgumentException(""); //TODO: StrategyNotFound)
                        });

    }
}
