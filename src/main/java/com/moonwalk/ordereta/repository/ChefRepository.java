package com.moonwalk.ordereta.repository;

import com.moonwalk.ordereta.entity.Chef;
import com.moonwalk.ordereta.enums.ChefStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChefRepository extends JpaRepository<Chef, Long> {
    List<Chef> findByRestaurantId(Long restaurantId);
    List<Chef> findByRestaurantIdAndStatus(Long restaurantId, ChefStatus status);
}
