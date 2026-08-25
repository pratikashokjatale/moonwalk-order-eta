package com.moonwalk.ordereta.config;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final ChefRepository chefRepository;
    private final CookingStationRepository stationRepository;
    private final DishRepository dishRepository;

    @Override
    public void run(String... args) throws Exception {
        if (restaurantRepository.count() == 0) {
            log.info("Database is empty. Seeding fresh fake data for MoonWalk Restaurant...");

            // 1. Create Restaurant
            Restaurant restaurant = Restaurant.builder()
                    .name("MoonWalk Original")
                    .active(true)
                    .strategy(EtaStrategyType.RESOURCE_AWARE)
                    .build();
            restaurant = restaurantRepository.save(restaurant);
            Long restId = restaurant.getId();

            // 2. Create Chefs
            chefRepository.save(Chef.builder().restaurantId(restId).name("Gordon Ramsay").status(ChefStatus.AVAILABLE).build());
            chefRepository.save(Chef.builder().restaurantId(restId).name("Jamie Oliver").status(ChefStatus.BUSY).build());
            chefRepository.save(Chef.builder().restaurantId(restId).name("Guy Fieri").status(ChefStatus.AVAILABLE).build());

            // 3. Create Stations
            stationRepository.save(CookingStation.builder().restaurantId(restId).name("Main Grill").type("GRILL").status(StationStatus.AVAILABLE).build());
            stationRepository.save(CookingStation.builder().restaurantId(restId).name("Wood Oven").type("OVEN").status(StationStatus.BUSY).build());
            stationRepository.save(CookingStation.builder().restaurantId(restId).name("Deep Fryer").type("FRYER").status(StationStatus.AVAILABLE).build());
            stationRepository.save(CookingStation.builder().restaurantId(restId).name("Drink Dispenser").type("DRINKS").status(StationStatus.AVAILABLE).build());

            // 4. Create Dishes
            dishRepository.save(Dish.builder().restaurantId(restId).name("Galactic Burger").preparationTime(12).requiredStation("GRILL").active(true).build());
            dishRepository.save(Dish.builder().restaurantId(restId).name("Meteor Pizza").preparationTime(18).requiredStation("OVEN").active(true).build());
            dishRepository.save(Dish.builder().restaurantId(restId).name("Asteroid Fries").preparationTime(5).requiredStation("FRYER").active(true).build());
            dishRepository.save(Dish.builder().restaurantId(restId).name("Nebula Cola").preparationTime(2).requiredStation("DRINKS").active(true).build());

            log.info("Fake data successfully seeded! Your test Restaurant ID is {}", restId);
        } else {
            log.info("Database already contains Original data.");
        }

        // Force create Mega Restaurant if it doesn't exist
        long megaCount = restaurantRepository.findAll().stream().filter(r -> r.getName().equals("MoonWalk Galaxy Mega")).count();
        if (megaCount == 0) {
            log.info("Seeding Mega Restaurant...");
            // 5. Create New Restaurant with large data
            Restaurant bigRestaurant = Restaurant.builder()
                    .name("MoonWalk Galaxy Mega")
                    .active(true)
                    .strategy(EtaStrategyType.RESOURCE_AWARE)
                    .build();
            bigRestaurant = restaurantRepository.save(bigRestaurant);
            Long bigRestId = bigRestaurant.getId();

            // 6. Create 15 Chefs
            String[] chefNames = {"Gordon Ramsay", "Jamie Oliver", "Marco Pierre White", "Thomas Keller", "Wolfgang Puck", 
                                  "Emeril Lagasse", "Bobby Flay", "Heston Blumenthal", "Ferran Adria", "Alain Ducasse", 
                                  "Anthony Bourdain", "Julia Child", "Jacques Pepin", "Paul Bocuse", "Rene Redzepi"};
            for (int i = 0; i < 15; i++) {
                chefRepository.save(Chef.builder().restaurantId(bigRestId).name(chefNames[i]).status(ChefStatus.AVAILABLE).build());
            }

            // 7. Create 10 Stations
            String[] stationNames = {"Main Grill", "Woodfire Oven", "Fryer Station A", "Fryer Station B", "Sauté Station", 
                                     "Salad Bar", "Beverage Dispenser", "Dessert Prep", "Wok Station", "Garnish Station"};
            String[] stationTypes = {"GRILL", "OVEN", "FRYER", "FRYER", "PREP", "PREP", "DRINKS", "PREP", "GRILL", "PREP"};
            for (int i = 0; i < 10; i++) {
                stationRepository.save(CookingStation.builder().restaurantId(bigRestId).name(stationNames[i]).type(stationTypes[i]).status(StationStatus.AVAILABLE).build());
            }

            // 8. Create 50 Dishes
            java.util.Random rand = new java.util.Random();
            String[] dishAdjectives = {"Spicy", "Crispy", "Smoked", "Grilled", "Classic", "Cheesy", "Truffle", "Garlic", "Sweet", "Savory"};
            String[] dishNouns = {"Burger", "Pizza", "Salad", "Pasta", "Tacos", "Wings", "Steak", "Soup", "Ribs", "Sandwich"};
            
            int dishCount = 0;
            for (String adj : dishAdjectives) {
                for (String noun : dishNouns) {
                    if (dishCount >= 50) break;
                    int prepTime = rand.nextInt(20) + 1; // 1 to 20 minutes
                    String type = stationTypes[rand.nextInt(stationTypes.length)];
                    dishRepository.save(Dish.builder()
                            .restaurantId(bigRestId)
                            .name(adj + " " + noun)
                            .preparationTime(prepTime)
                            .requiredStation(type)
                            .active(true)
                            .build());
                    dishCount++;
                }
            }
            log.info("Mega data successfully seeded! Your Mega Restaurant ID is {}", bigRestId);
        } else {
            log.info("Mega Restaurant already exists. Skipping.");
        }
    }
}
