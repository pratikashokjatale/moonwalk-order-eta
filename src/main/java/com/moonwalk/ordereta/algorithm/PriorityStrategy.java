package com.moonwalk.ordereta.algorithm;

import com.moonwalk.ordereta.entity.Order;
import com.moonwalk.ordereta.enums.EtaStrategyType;
import org.springframework.stereotype.Component;

@Component
public class PriorityStrategy implements EtaStrategy {

    @Override
    public EtaStrategyType getType() {
        return EtaStrategyType.PRIORITY;
    }

    @Override
    public EtaResult calculateEta(Order newOrder, KitchenState kitchenState) {
        // Mocking priority for now. Real implementation would check a priority field on the Order or Customer.
        // Assuming this is a priority order, we skip the queue delay partially.
        long delayMinutes = 15; // default fixed for VIP processing

        return EtaResult.builder()
                .estimatedTimeSeconds(delayMinutes * 60)
                .build();
    }
}
