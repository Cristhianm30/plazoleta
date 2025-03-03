package com.plaza.plazoleta.infraestructure.repository;

import com.plaza.plazoleta.domain.model.Dish;
import com.plaza.plazoleta.domain.port.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DishRepositoryImpl implements DishRepository {

    @Autowired
    private JpaDishRepository jpaDishRepository;

    @Override
    public Dish save(Dish dish) {
        return jpaDishRepository.save(dish);
    }
}
