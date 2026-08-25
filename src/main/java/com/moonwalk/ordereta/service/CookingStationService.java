package com.moonwalk.ordereta.service;

import com.moonwalk.ordereta.entity.CookingStation;
import com.moonwalk.ordereta.enums.StationStatus;
import com.moonwalk.ordereta.exception.ResourceNotFoundException;
import com.moonwalk.ordereta.repository.CookingStationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CookingStationService {

    private final CookingStationRepository cookingStationRepository;

    @Transactional
    public CookingStation createStation(CookingStation station) {
        return cookingStationRepository.save(station);
    }

    public List<CookingStation> getStationsByRestaurant(Long restaurantId) {
        return cookingStationRepository.findByRestaurantId(restaurantId);
    }

    @Transactional
    public CookingStation updateStatus(Long id, StationStatus status) {
        CookingStation station = cookingStationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cooking station not found"));
        station.setStatus(status);
        return cookingStationRepository.save(station);
    }
}
