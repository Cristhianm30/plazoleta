package com.plaza.plazoleta;

import com.plaza.plazoleta.domain.model.Category;
import com.plaza.plazoleta.domain.model.Dish;
import com.plaza.plazoleta.domain.model.Restaurant;

public class DishDataProvider {

    // Mock de plato válido (todos los campos correctos)
    public static Dish validDishMock() {
        return Dish.builder()
                .name("Pizza Margarita")
                .price(15.99)
                .description("Pizza clásica con mozzarella y albahaca")
                .imageUrl("http://example.com/pizza.jpg")
                .category(validCategoryMock()) // Usar método interno
                .restaurant(validRestaurantMock()) // Usar método interno
                .build();
    }

    // Mock de plato con precio inválido (negativo)
    public static Dish invalidPriceDishMock() {
        Dish dish = validDishMock(); // Reutilizar mock válido
        dish.setPrice(-10.0);
        return dish;
    }

    // Mock de plato sin campos obligatorios (todos nulos)
    public static Dish missingAllRequiredFieldsDishMock() {
        return Dish.builder().build();
    }

    // Mock de plato sin nombre
    public static Dish missingNameDishMock() {
        Dish dish = validDishMock();
        dish.setName(null);
        return dish;
    }

    // Mock de plato sin descripción
    public static Dish missingDescriptionDishMock() {
        Dish dish = validDishMock();
        dish.setDescription(null);
        return dish;
    }

    // Mock de categoría válida (para reutilización)
    public static Category validCategoryMock() {
        return Category.builder()
                .id(1L)
                .name("Italiana")
                .description("Platos tradicionales italianos")
                .build();
    }

    // Mock de restaurante válido (para reutilización)
    public static Restaurant validRestaurantMock() {
        return Restaurant.builder()
                .id(1L)
                .name("Trattoria Italia")
                .nit("123456789")
                .address("Calle 123")
                .cellPhone("+573001234567")
                .urlLogo("http://example.com/logo.png")
                .userId(1L)
                .build();
    }
}