package com.plaza.plazoleta.infraestructure.repository;

import com.plaza.plazoleta.domain.model.Dish;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaDishRepository extends JpaRepository<Dish, Long> {

}
