package com.moonwalk.ordereta.service;

import com.moonwalk.ordereta.entity.Chef;
import com.moonwalk.ordereta.enums.ChefStatus;
import com.moonwalk.ordereta.exception.ResourceNotFoundException;
import com.moonwalk.ordereta.repository.ChefRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChefService {

    private final ChefRepository chefRepository;

    @Transactional
    public Chef createChef(Chef chef) {
        return chefRepository.save(chef);
    }

    public List<Chef> getChefsByRestaurant(Long restaurantId) {
        return chefRepository.findByRestaurantId(restaurantId);
    }

    @Transactional
    public Chef updateStatus(Long id, ChefStatus status) {
        Chef chef = chefRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chef not found"));
        chef.setStatus(status);
        return chefRepository.save(chef);
    }

    @Transactional
    public Chef updateWorkload(Long id, Integer workload) {
        Chef chef = chefRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chef not found"));
        chef.setCurrentWorkload(workload);
        return chefRepository.save(chef);
    }
}
