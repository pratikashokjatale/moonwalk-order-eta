package com.moonwalk.ordereta.service;

import com.moonwalk.ordereta.dto.response.CountdownResponse;
import com.moonwalk.ordereta.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CountdownService {

    private final OrderService orderService;

    public CountdownResponse getCountdown(Long orderId) {
        Order order = orderService.getOrder(orderId);
        
        if (order.getEstimatedAt() == null || order.getEstimatedTimeSeconds() == null) {
            return new CountdownResponse(orderId, 0L, 0L, 0L);
        }

        LocalDateTime now = LocalDateTime.now();
        long elapsedSeconds = Duration.between(order.getEstimatedAt(), now).getSeconds();
        long remainingSeconds = order.getEstimatedTimeSeconds() - elapsedSeconds;
        
        if (remainingSeconds < 0) {
            remainingSeconds = 0;
        }

        return new CountdownResponse(orderId, order.getEstimatedTimeSeconds(), elapsedSeconds, remainingSeconds);
    }
}
