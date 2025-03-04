package com.plaza.plazoleta.infraestructure.controller;


import com.plaza.plazoleta.application.exception.NotFoundException;
import com.plaza.plazoleta.application.exception.UnauthorizedException;
import com.plaza.plazoleta.domain.model.Dish;

import com.plaza.plazoleta.domain.service.DishService;

import com.plaza.plazoleta.infraestructure.dto.UpdateDishDto;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dish")
public class DishController {

    private final DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    @PostMapping
    @PreAuthorize("hasRole('PROPIETARIO')")
    public ResponseEntity<Dish> createDish(@RequestBody Dish dish) {
        Dish createdDish = dishService.createDish(dish); // ✅ Sin parámetro de token
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDish);
    }

    @PatchMapping("/update/{id}")
    @PreAuthorize("hasRole('PROPIETARIO')")
    public ResponseEntity<Dish> updateDish(@PathVariable Long id,@RequestBody UpdateDishDto request) {

            Dish updatedDish = dishService.updateDish(id,request);
            return ResponseEntity.ok(updatedDish);
    }
}
