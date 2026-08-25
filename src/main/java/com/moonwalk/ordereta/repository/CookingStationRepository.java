package com.moonwalk.ordereta.repository;

import com.moonwalk.ordereta.entity.CookingStation;
import com.moonwalk.ordereta.enums.StationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CookingStationRepository extends JpaRepository<CookingStation, Long> {
    List<CookingStation> findByRestaurantId(Long restaurantId);
    List<CookingStation> findByRestaurantIdAndStatus(Long restaurantId, StationStatus status);
}
