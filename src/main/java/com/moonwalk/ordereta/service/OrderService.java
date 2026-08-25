package com.moonwalk.ordereta.service;

import com.moonwalk.ordereta.algorithm.EtaEngine;
import com.moonwalk.ordereta.algorithm.EtaResult;
import com.moonwalk.ordereta.algorithm.KitchenState;
import com.moonwalk.ordereta.entity.EtaExecution;
import com.moonwalk.ordereta.entity.Order;
import com.moonwalk.ordereta.entity.Restaurant;
import com.moonwalk.ordereta.enums.OrderStatus;
import com.moonwalk.ordereta.enums.StationStatus;
import com.moonwalk.ordereta.exception.ResourceNotFoundException;
import com.moonwalk.ordereta.repository.EtaExecutionRepository;
import com.moonwalk.ordereta.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KitchenService kitchenService;
    private final RestaurantService restaurantService;
    private final EtaEngine etaEngine;
    private final EtaExecutionRepository etaExecutionRepository;
    private final com.moonwalk.ordereta.repository.ChefRepository chefRepository;
    private final com.moonwalk.ordereta.repository.CookingStationRepository cookingStationRepository;

    @Transactional
    public Order createOrder(Order order) {
        Restaurant restaurant = restaurantService.getRestaurant(order.getRestaurantId());

        KitchenState state = kitchenService.getCurrentState(restaurant.getId());

        EtaResult etaResult = etaEngine.calculate(restaurant.getStrategy(), order, state);

        order.setStatus(OrderStatus.IN_QUEUE);
        order.setEstimatedTimeSeconds(etaResult.getEstimatedTimeSeconds());
        order.setEstimatedAt(LocalDateTime.now());
        order.setAlgorithmUsed(restaurant.getStrategy());
        
        Order savedOrder = orderRepository.save(order);

        // Save ETA execution
        EtaExecution execution = EtaExecution.builder()
                .orderId(savedOrder.getId())
                .estimatedTimeSeconds(etaResult.getEstimatedTimeSeconds())
                .algorithmUsed(restaurant.getStrategy())
                .pendingOrderCount(state.getPendingOrders().size())
                .availableChefCount(state.getAvailableChefs().size())
                .busyStationCount((int) state.getAllStations().stream().filter(s -> s.getStatus() == StationStatus.BUSY).count())
                .status("CALCULATED")
                .build();
        etaExecutionRepository.save(execution);

        // Assign this order's workload to the chef with the lowest current workload
        if (state.getAvailableChefs() != null && !state.getAvailableChefs().isEmpty()) {
            com.moonwalk.ordereta.entity.Chef leastBusyChef = state.getAvailableChefs().stream()
                    .min(java.util.Comparator.comparingInt(c -> c.getCurrentWorkload() != null ? c.getCurrentWorkload() : 0))
                    .orElse(state.getAvailableChefs().get(0));
            
            int orderWorkload = order.getItems() != null ? 
                    order.getItems().stream().mapToInt(com.moonwalk.ordereta.entity.OrderItem::getPreparationTime).sum() : 0;
            
            if (orderWorkload == 0) {
                orderWorkload = 1; // default fallback if no items or times provided
            }

            int current = leastBusyChef.getCurrentWorkload() != null ? leastBusyChef.getCurrentWorkload() : 0;
            leastBusyChef.setCurrentWorkload(current + orderWorkload);
            chefRepository.save(leastBusyChef);
        }

        // Assign this order's workload to the cooking station with the lowest current workload
        if (state.getAllStations() != null && !state.getAllStations().isEmpty()) {
            com.moonwalk.ordereta.entity.CookingStation leastBusyStation = state.getAllStations().stream()
                    .min(java.util.Comparator.comparingInt(s -> s.getCurrentWorkload() != null ? s.getCurrentWorkload() : 0))
                    .orElse(state.getAllStations().get(0));

            int orderWorkload = order.getItems() != null ? 
                    order.getItems().stream().mapToInt(com.moonwalk.ordereta.entity.OrderItem::getPreparationTime).sum() : 0;
            
            if (orderWorkload == 0) {
                orderWorkload = 1; 
            }

            int current = leastBusyStation.getCurrentWorkload() != null ? leastBusyStation.getCurrentWorkload() : 0;
            leastBusyStation.setCurrentWorkload(current + orderWorkload);
            cookingStationRepository.save(leastBusyStation);
        }

        return savedOrder;
    }

    @Transactional
    public Order updateOrder(Order order) {
        Restaurant restaurant = restaurantService.getRestaurant(order.getRestaurantId());
        KitchenState state = kitchenService.getCurrentState(restaurant.getId());

        // Recalculate ETA based on new items
        EtaResult etaResult = etaEngine.calculate(restaurant.getStrategy(), order, state);

        order.setEstimatedTimeSeconds(etaResult.getEstimatedTimeSeconds());
        order.setEstimatedAt(LocalDateTime.now());
        
        Order savedOrder = orderRepository.save(order);

        // Save ETA execution history for this update
        EtaExecution execution = EtaExecution.builder()
                .orderId(savedOrder.getId())
                .estimatedTimeSeconds(etaResult.getEstimatedTimeSeconds())
                .algorithmUsed(restaurant.getStrategy())
                .pendingOrderCount(state.getPendingOrders().size())
                .availableChefCount(state.getAvailableChefs().size())
                .busyStationCount((int) state.getAllStations().stream().filter(s -> s.getStatus() == StationStatus.BUSY).count())
                .status("UPDATED")
                .build();
        etaExecutionRepository.save(execution);

        // Note: For a fully accurate system, we would subtract the old workload from the chefs/stations before adding the new workload here. 
        // For simplicity in this demo, we assume updating an order recalculates ETA but doesn't fundamentally change the global workload assignment tracking.

        return savedOrder;
    }

    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }

    public java.util.List<Order> getOrdersByRestaurant(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = getOrder(orderId);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public java.util.List<EtaExecution> getEtaHistory(Long orderId) {
        return etaExecutionRepository.findByOrderIdOrderByTimestampAsc(orderId);
    }
}
