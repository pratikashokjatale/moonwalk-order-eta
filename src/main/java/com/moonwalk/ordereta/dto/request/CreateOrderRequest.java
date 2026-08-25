package com.moonwalk.ordereta.dto.request;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {
    private Long restaurantId;
    private Long customerId;
    private List<OrderItemRequest> items;
}
