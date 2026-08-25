package com.moonwalk.ordereta.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long dishId;
    private int quantity;
}
