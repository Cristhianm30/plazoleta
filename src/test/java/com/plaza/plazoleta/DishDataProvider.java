package com.plaza.plazoleta;

import com.plaza.plazoleta.domain.model.Category;
import com.plaza.plazoleta.domain.model.Dish;
import com.plaza.plazoleta.domain.model.Restaurant;

public class DishDataProvider {


    public static Dish validDishMock() {
        return Dish.builder()
                .name("Pizza Margarita")
                .price(1599)
                .description("Pizza clásica con mozzarella y albahaca")
                .imageUrl("http://example.com/pizza.jpg")
                .category(validCategoryMock()) // Usar método interno
                .restaurant(validRestaurantMock()) // Usar método interno
                .build();
    }


    public static Dish invalidPriceDishMock() {
        Dish dish = validDishMock(); // Reutilizar mock válido
        dish.setPrice(-100);
        return dish;
    }


    public static Dish missingNameDishMock() {
        Dish dish = validDishMock();
        dish.setName(null);
        return dish;
    }


    public static Dish missingDescriptionDishMock() {
        Dish dish = validDishMock();
        dish.setDescription(null);
        return dish;
    }


    public static Category validCategoryMock() {
        return Category.builder()
                .id(1L)
                .name("Italiana")
                .description("Platos tradicionales italianos")
                .build();
    }


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