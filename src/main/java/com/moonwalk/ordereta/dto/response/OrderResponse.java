package com.moonwalk.ordereta.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Long orderId;
    private String status;
    private Long estimatedTimeMinutes;
    private Long remainingSeconds;
    private String algorithm;
}
