package com.moonwalk.ordereta.repository;

import com.moonwalk.ordereta.entity.Order;
import com.moonwalk.ordereta.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRestaurantIdAndStatusIn(Long restaurantId, List<OrderStatus> statuses);
    List<Order> findByRestaurantId(Long restaurantId);
}
