package com.plaza.plazoleta.domain.port;

import com.plaza.plazoleta.domain.model.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {
    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findByName(String name);
}
