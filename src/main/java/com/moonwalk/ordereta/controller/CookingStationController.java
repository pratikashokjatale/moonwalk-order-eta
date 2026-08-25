package com.moonwalk.ordereta.controller;

import com.moonwalk.ordereta.entity.CookingStation;
import com.moonwalk.ordereta.service.CookingStationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CookingStationController {

    private final CookingStationService cookingStationService;

    @PostMapping("/restaurants/{restaurantId}/stations")
    public CookingStation addStation(@PathVariable Long restaurantId, @RequestBody CookingStation station) {
        station.setRestaurantId(restaurantId);
        return cookingStationService.createStation(station);
    }

    @GetMapping("/restaurants/{restaurantId}/stations")
    public List<CookingStation> getStations(@PathVariable Long restaurantId) {
        return cookingStationService.getStationsByRestaurant(restaurantId);
    }
}
