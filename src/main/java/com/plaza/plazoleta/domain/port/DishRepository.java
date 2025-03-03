package com.plaza.plazoleta.domain.port;

import com.plaza.plazoleta.domain.model.Dish;

import java.util.Optional;

public interface DishRepository {
    Dish save(Dish dish);

}
