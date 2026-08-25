package com.moonwalk.ordereta.algorithm;

import com.moonwalk.ordereta.entity.Order;
import com.moonwalk.ordereta.entity.OrderItem;
import com.moonwalk.ordereta.enums.EtaStrategyType;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class ShortestJobStrategy implements EtaStrategy {

    @Override
    public EtaStrategyType getType() {
        return EtaStrategyType.SJF;
    }

    @Override
    public EtaResult calculateEta(Order newOrder, KitchenState kitchenState) {
        long newOrderTime = getMaxPrepTime(newOrder);
        long delayMinutes = 0;

        // Only add delay for pending orders that are shorter than or equal to the new order
        // (Assuming they get processed first)
        for (Order pendingOrder : kitchenState.getPendingOrders()) {
            long pendingTime = getMaxPrepTime(pendingOrder);
            if (pendingTime <= newOrderTime) {
                delayMinutes += pendingTime;
            }
        }
        
        // Add the new order's own time
        delayMinutes += newOrderTime;

        return EtaResult.builder()
                .estimatedTimeSeconds(delayMinutes * 60)
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
