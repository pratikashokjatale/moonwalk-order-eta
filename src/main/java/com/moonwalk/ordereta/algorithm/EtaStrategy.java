package com.moonwalk.ordereta.algorithm;

import com.moonwalk.ordereta.entity.Order;
import com.moonwalk.ordereta.enums.EtaStrategyType;

public interface EtaStrategy {
    EtaStrategyType getType();
    EtaResult calculateEta(Order newOrder, KitchenState kitchenState);
}
