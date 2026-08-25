package com.moonwalk.ordereta.service;

import com.moonwalk.ordereta.algorithm.KitchenState;
import com.moonwalk.ordereta.entity.Chef;
import com.moonwalk.ordereta.entity.CookingStation;
import com.moonwalk.ordereta.entity.Order;
import com.moonwalk.ordereta.enums.ChefStatus;
import com.moonwalk.ordereta.enums.OrderStatus;
import com.moonwalk.ordereta.repository.ChefRepository;
import com.moonwalk.ordereta.repository.CookingStationRepository;
import com.moonwalk.ordereta.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KitchenService {

    private final OrderRepository orderRepository;
    private final ChefRepository chefRepository;
    private final CookingStationRepository cookingStationRepository;

    public KitchenState getCurrentState(Long restaurantId) {
        List<Order> pendingOrders = orderRepository.findByRestaurantIdAndStatusIn(
                restaurantId, Arrays.asList(OrderStatus.NEW, OrderStatus.IN_QUEUE, OrderStatus.COOKING)
        );

        List<Chef> availableChefs = chefRepository.findByRestaurantIdAndStatus(restaurantId, ChefStatus.AVAILABLE);
        
        List<CookingStation> allStations = cookingStationRepository.findByRestaurantId(restaurantId);

        return KitchenState.builder()
                .pendingOrders(pendingOrders)
                .availableChefs(availableChefs)
                .allStations(allStations)
                .build();
    }
}
