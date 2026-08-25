package com.moonwalk.ordereta.controller;

import com.moonwalk.ordereta.dto.request.CreateOrderRequest;
import com.moonwalk.ordereta.dto.request.OrderItemRequest;
import com.moonwalk.ordereta.dto.response.CountdownResponse;
import com.moonwalk.ordereta.dto.response.OrderResponse;
import com.moonwalk.ordereta.entity.Dish;
import com.moonwalk.ordereta.entity.Order;
import com.moonwalk.ordereta.entity.OrderItem;
import com.moonwalk.ordereta.service.CountdownService;
import com.moonwalk.ordereta.service.DishService;
import com.moonwalk.ordereta.service.OrderService;
import com.moonwalk.ordereta.entity.EtaExecution;
import com.moonwalk.ordereta.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final CountdownService countdownService;
    private final DishService dishService;

    @PostMapping("/restaurants/{restaurantId}/orders")
    public OrderResponse createOrder(@PathVariable Long restaurantId, @RequestBody CreateOrderRequest request) {
        Order newOrder = Order.builder()
                .restaurantId(restaurantId)
                .customerId(request.getCustomerId())
                .items(new ArrayList<>())
                .build();
                
        // In a real app we'd map this better, but we need prep time from the Dish
        var allDishes = dishService.getDishesByRestaurant(restaurantId);

        for (OrderItemRequest itemReq : request.getItems()) {
            Dish dish = allDishes.stream()
                .filter(d -> d.getId().equals(itemReq.getDishId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Dish not found: " + itemReq.getDishId()));
            
            OrderItem item = OrderItem.builder()
                    .dishId(dish.getId())
                    .quantity(itemReq.getQuantity())
                    .preparationTime(dish.getPreparationTime())
                    .build();
            newOrder.addItem(item);
        }

        Order savedOrder = orderService.createOrder(newOrder);
        
        long estimatedTimeMinutes = savedOrder.getEstimatedTimeSeconds() != null ? savedOrder.getEstimatedTimeSeconds() / 60 : 0;
        
        return OrderResponse.builder()
                .orderId(savedOrder.getId())
                .status(savedOrder.getStatus().name())
                .estimatedTimeMinutes(estimatedTimeMinutes)
                .remainingSeconds(savedOrder.getEstimatedTimeSeconds())
                .algorithm(savedOrder.getAlgorithmUsed().name())
                .build();
    }

    @GetMapping("/orders/{orderId}/countdown")
    public CountdownResponse getCountdown(@PathVariable Long orderId) {
        return countdownService.getCountdown(orderId);
    }

    @GetMapping("/restaurants/{restaurantId}/orders")
    public List<Order> getOrdersByRestaurant(@PathVariable Long restaurantId) {
        return orderService.getOrdersByRestaurant(restaurantId);
    }

    @GetMapping("/orders/{orderId}")
    public Order getOrder(@PathVariable Long orderId) {
        return orderService.getOrder(orderId);
    }

    @PutMapping("/orders/{orderId}/status")
    public Order updateOrderStatus(@PathVariable Long orderId, @RequestParam OrderStatus status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    @PutMapping("/orders/{orderId}")
    public OrderResponse updateOrder(@PathVariable Long orderId, @RequestBody CreateOrderRequest request) {
        Order existingOrder = orderService.getOrder(orderId);
        
        // Clear existing items
        existingOrder.getItems().clear();
        
        var allDishes = dishService.getDishesByRestaurant(existingOrder.getRestaurantId());

        for (OrderItemRequest itemReq : request.getItems()) {
            Dish dish = allDishes.stream()
                .filter(d -> d.getId().equals(itemReq.getDishId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Dish not found: " + itemReq.getDishId()));
            
            OrderItem item = OrderItem.builder()
                    .dishId(dish.getId())
                    .quantity(itemReq.getQuantity())
                    .preparationTime(dish.getPreparationTime())
                    .build();
            existingOrder.addItem(item);
        }

        // Recalculate ETA and save using the service
        Order updatedOrder = orderService.updateOrder(existingOrder);
        
        long estimatedTimeMinutes = updatedOrder.getEstimatedTimeSeconds() != null ? updatedOrder.getEstimatedTimeSeconds() / 60 : 0;
        
        return OrderResponse.builder()
                .orderId(updatedOrder.getId())
                .status(updatedOrder.getStatus().name())
                .estimatedTimeMinutes(estimatedTimeMinutes)
                .remainingSeconds(updatedOrder.getEstimatedTimeSeconds())
                .algorithm(updatedOrder.getAlgorithmUsed().name())
                .build();
    }

    @GetMapping("/orders/{orderId}/eta-history")
    public List<EtaExecution> getEtaHistory(@PathVariable Long orderId) {
        return orderService.getEtaHistory(orderId);
    }
}
