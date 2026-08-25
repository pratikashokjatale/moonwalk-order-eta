package com.moonwalk.ordereta.algorithm;

import com.moonwalk.ordereta.entity.Order;
import com.moonwalk.ordereta.enums.EtaStrategyType;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EtaEngine {

    private final Map<EtaStrategyType, EtaStrategy> strategies;

    public EtaEngine(List<EtaStrategy> strategyList) {
        strategies = strategyList.stream()
                .collect(Collectors.toMap(EtaStrategy::getType, strategy -> strategy));
    }

    public EtaResult calculate(EtaStrategyType type, Order newOrder, KitchenState state) {
        EtaStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy not implemented: " + type);
        }
        return strategy.calculateEta(newOrder, state);
    }
}
