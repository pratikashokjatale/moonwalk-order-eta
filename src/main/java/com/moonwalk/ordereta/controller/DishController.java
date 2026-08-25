package com.moonwalk.ordereta.controller;

import com.moonwalk.ordereta.entity.Dish;
import com.moonwalk.ordereta.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DishController {

    private final DishService dishService;

    @PostMapping("/restaurants/{restaurantId}/dishes")
    public Dish addDish(@PathVariable Long restaurantId, @RequestBody Dish dish) {
        dish.setRestaurantId(restaurantId);
        return dishService.createDish(dish);
    }

    @GetMapping("/restaurants/{restaurantId}/dishes")
    public List<Dish> getDishes(@PathVariable Long restaurantId) {
        return dishService.getDishesByRestaurant(restaurantId);
    }
}
