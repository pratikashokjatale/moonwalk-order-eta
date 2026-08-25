package com.moonwalk.ordereta.repository;

import com.moonwalk.ordereta.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findByRestaurantIdAndActiveTrue(Long restaurantId);
}
