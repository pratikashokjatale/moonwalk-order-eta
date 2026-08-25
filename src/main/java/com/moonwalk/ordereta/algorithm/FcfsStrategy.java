package com.moonwalk.ordereta.algorithm;

import com.moonwalk.ordereta.entity.Order;
import com.moonwalk.ordereta.entity.OrderItem;
import com.moonwalk.ordereta.enums.EtaStrategyType;
import org.springframework.stereotype.Component;

@Component
public class FcfsStrategy implements EtaStrategy {

    @Override
    public EtaStrategyType getType() {
        return EtaStrategyType.FCFS;
    }

    @Override
    public EtaResult calculateEta(Order newOrder, KitchenState kitchenState) {
        long totalDelayMinutes = 0;

        // Sum up max preparation time for all pending orders
        for (Order pendingOrder : kitchenState.getPendingOrders()) {
            totalDelayMinutes += getMaxPrepTime(pendingOrder);
        }

        // Add max preparation time for the new order
        totalDelayMinutes += getMaxPrepTime(newOrder);

        return EtaResult.builder()
                .estimatedTimeSeconds(totalDelayMinutes * 60)
                .build();
    }

    private long getMaxPrepTime(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) return 0;
        return order.getItems().stream()
                .mapToLong(OrderItem::getPreparationTime)
                .max()
                .orElse(0);
    }
}
