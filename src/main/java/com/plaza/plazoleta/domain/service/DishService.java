package com.plaza.plazoleta.domain.service;


import com.plaza.plazoleta.domain.model.Dish;
import com.plaza.plazoleta.infraestructure.dto.StatusDishDto;
import com.plaza.plazoleta.infraestructure.dto.UpdateDishDto;

public interface DishService {
    Dish createDish(Dish dish);
    Dish updateDish(Long dishId, UpdateDishDto updateDishDto);
    Dish updateDishStatus(Long dishId, StatusDishDto statusDishDto);
}
