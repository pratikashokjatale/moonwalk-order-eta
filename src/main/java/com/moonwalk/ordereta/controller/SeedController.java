package com.moonwalk.ordereta.controller;

import com.moonwalk.ordereta.entity.Chef;
import com.moonwalk.ordereta.entity.CookingStation;
import com.moonwalk.ordereta.entity.Dish;
import com.moonwalk.ordereta.entity.Restaurant;
import com.moonwalk.ordereta.enums.ChefStatus;
import com.moonwalk.ordereta.enums.EtaStrategyType;
import com.moonwalk.ordereta.enums.StationStatus;
import com.moonwalk.ordereta.repository.ChefRepository;
import com.moonwalk.ordereta.repository.CookingStationRepository;
import com.moonwalk.ordereta.repository.DishRepository;
import com.moonwalk.ordereta.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/api/seed")
@RequiredArgsConstructor
public class SeedController {

    private final RestaurantRepository restaurantRepository;
    private final ChefRepository chefRepository;
    private final CookingStationRepository stationRepository;
    private final DishRepository dishRepository;

    @PostMapping("/restaurant")
    public Restaurant seedNewRestaurant(@RequestParam(defaultValue = "MoonWalk Dynamic Restaurant") String name) {
        
        Restaurant restaurant = Restaurant.builder()
                .name(name)
                .active(true)
                .strategy(EtaStrategyType.RESOURCE_AWARE)
                .build();
        restaurant = restaurantRepository.save(restaurant);
        Long restId = restaurant.getId();

        String[] chefNames = {"Gordon Ramsay", "Jamie Oliver", "Marco Pierre White", "Thomas Keller", "Wolfgang Puck", 
                              "Emeril Lagasse", "Bobby Flay", "Heston Blumenthal", "Ferran Adria", "Alain Ducasse", 
                              "Anthony Bourdain", "Julia Child", "Jacques Pepin", "Paul Bocuse", "Rene Redzepi"};
        for (int i = 0; i < 15; i++) {
            chefRepository.save(Chef.builder().restaurantId(restId).name(chefNames[i]).status(ChefStatus.AVAILABLE).build());
        }

        String[] stationNames = {"Main Grill", "Woodfire Oven", "Fryer Station A", "Fryer Station B", "Sauté Station", 
                                 "Salad Bar", "Beverage Dispenser", "Dessert Prep", "Wok Station", "Garnish Station"};
        String[] stationTypes = {"GRILL", "OVEN", "FRYER", "FRYER", "PREP", "PREP", "DRINKS", "PREP", "GRILL", "PREP"};
        for (int i = 0; i < 10; i++) {
            stationRepository.save(CookingStation.builder().restaurantId(restId).name(stationNames[i]).type(stationTypes[i]).status(StationStatus.AVAILABLE).build());
        }

        Random rand = new Random();
        String[] dishAdjectives = {"Spicy", "Crispy", "Smoked", "Grilled", "Classic", "Cheesy", "Truffle", "Garlic", "Sweet", "Savory"};
        String[] dishNouns = {"Burger", "Pizza", "Salad", "Pasta", "Tacos", "Wings", "Steak", "Soup", "Ribs", "Sandwich"};
        
        int dishCount = 0;
        for (String adj : dishAdjectives) {
            for (String noun : dishNouns) {
                if (dishCount >= 50) break;
                int prepTime = rand.nextInt(20) + 1; // 1 to 20 minutes
                String type = stationTypes[rand.nextInt(stationTypes.length)];
                dishRepository.save(Dish.builder()
                        .restaurantId(restId)
                        .name(adj + " " + noun)
                        .preparationTime(prepTime)
                        .requiredStation(type)
                        .active(true)
                        .build());
                dishCount++;
            }
        }

        return restaurant;
    }
}
