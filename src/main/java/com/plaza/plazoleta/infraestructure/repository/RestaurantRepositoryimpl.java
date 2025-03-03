package com.plaza.plazoleta.infraestructure.repository;

import com.plaza.plazoleta.domain.model.Restaurant;
import com.plaza.plazoleta.domain.port.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class RestaurantRepositoryimpl implements RestaurantRepository {

    @Autowired
    private JpaRestaurantRepository jpaRestaurantRepository;

    @Override
    public Restaurant save(Restaurant restaurant) {
        return jpaRestaurantRepository.save(restaurant);
    }

    @Override
    public Optional<Restaurant> findByUserId(Long userId) {
        return jpaRestaurantRepository.findByUserId(userId);
    }

    @Override
    public Optional<Restaurant> findByName(String name) {
        return jpaRestaurantRepository.findByName(name);
    }
}
