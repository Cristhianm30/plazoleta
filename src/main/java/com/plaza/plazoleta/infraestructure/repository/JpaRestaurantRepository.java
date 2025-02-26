package com.plaza.plazoleta.infraestructure.repository;

import com.plaza.plazoleta.domain.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaRestaurantRepository extends JpaRepository<Restaurant, Long> {



    Optional<Restaurant> findByName(String name);
}
