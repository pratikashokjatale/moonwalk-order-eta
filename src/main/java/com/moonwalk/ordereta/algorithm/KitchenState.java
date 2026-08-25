package com.moonwalk.ordereta.algorithm;

import com.moonwalk.ordereta.entity.Chef;
import com.moonwalk.ordereta.entity.CookingStation;
import com.moonwalk.ordereta.entity.Order;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class KitchenState {
    private List<Order> pendingOrders;
    private List<Chef> availableChefs;
    private List<CookingStation> allStations;
}
