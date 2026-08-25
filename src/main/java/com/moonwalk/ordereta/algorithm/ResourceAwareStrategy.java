package com.moonwalk.ordereta.algorithm;

import com.moonwalk.ordereta.entity.Order;
import com.moonwalk.ordereta.entity.OrderItem;
import com.moonwalk.ordereta.enums.EtaStrategyType;
import org.springframework.stereotype.Component;

@Component
public class ResourceAwareStrategy implements EtaStrategy {

    @Override
    public EtaStrategyType getType() {
        return EtaStrategyType.RESOURCE_AWARE;
    }

    @Override
    public EtaResult calculateEta(Order newOrder, KitchenState kitchenState) {
        long baseTime = getMaxPrepTime(newOrder);

        // Calculate queue delay
        long queueDelay = 0;
        for (Order pendingOrder : kitchenState.getPendingOrders()) {
            queueDelay += getMaxPrepTime(pendingOrder);
        }

        // Apply resource penalties
        long chefPenalty = 0;
        long leastBusyChefWorkload = 0;

        if (kitchenState.getAvailableChefs() == null || kitchenState.getAvailableChefs().isEmpty()) {
            chefPenalty = 10; // 10 minutes delay if no chefs available
        } else {
            if (kitchenState.getAvailableChefs().size() < kitchenState.getPendingOrders().size() / 2) {
                chefPenalty = 5; // moderate delay if understaffed
            }
            // Add penalty based on the chef who will receive this order (least busy chef)
            leastBusyChefWorkload = kitchenState.getAvailableChefs().stream()
                    .mapToLong(chef -> chef.getCurrentWorkload() != null ? chef.getCurrentWorkload() : 0)
                    .min()
                    .orElse(0);
        }

        // Just simulating station availability delay
        long stationPenalty = 0;
        long busyStationCount = kitchenState.getAllStations().stream()
                .filter(s -> s.getStatus().name().equals("BUSY"))
                .count();
        if (busyStationCount > 0) {
            stationPenalty = busyStationCount * 2; // 2 minutes delay per busy station
        }

        // If it's a very quick item like a Coke (base time <= 2), skip the massive queue delays
        if (baseTime <= 2 && baseTime > 0) {
            long quickDelay = baseTime + (queueDelay / 10) + stationPenalty; 
            return EtaResult.builder()
                    .estimatedTimeSeconds(quickDelay * 60)
                    .build();
        }

        // We add leastBusyChefWorkload to account for the current work assigned to the assigned chef
        long totalDelayMinutes = baseTime + (queueDelay / 2) + chefPenalty + stationPenalty + leastBusyChefWorkload;

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
