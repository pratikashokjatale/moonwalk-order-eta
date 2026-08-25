package com.moonwalk.ordereta.service;

import com.moonwalk.ordereta.entity.Dish;
import com.moonwalk.ordereta.repository.DishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    @Transactional
    public Dish createDish(Dish dish) {
        return dishRepository.save(dish);
    }

    public List<Dish> getDishesByRestaurant(Long restaurantId) {
        return dishRepository.findByRestaurantIdAndActiveTrue(restaurantId);
    }
}
