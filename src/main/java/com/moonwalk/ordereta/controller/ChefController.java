package com.moonwalk.ordereta.controller;

import com.moonwalk.ordereta.entity.Chef;
import com.moonwalk.ordereta.service.ChefService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChefController {

    private final ChefService chefService;

    @PostMapping("/restaurants/{restaurantId}/chefs")
    public Chef addChef(@PathVariable Long restaurantId, @RequestBody Chef chef) {
        chef.setRestaurantId(restaurantId);
        return chefService.createChef(chef);
    }

    @GetMapping("/restaurants/{restaurantId}/chefs")
    public List<Chef> getChefs(@PathVariable Long restaurantId) {
        return chefService.getChefsByRestaurant(restaurantId);
    }

    @PatchMapping("/chefs/{id}/workload")
    public Chef updateChefWorkload(@PathVariable Long id, @RequestParam Integer workload) {
        return chefService.updateWorkload(id, workload);
    }
}
