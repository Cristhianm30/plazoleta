package com.plaza.plazoleta.application.service;


import com.plaza.plazoleta.application.exception.InvalidPriceException;
import com.plaza.plazoleta.application.exception.NotFoundException;
import com.plaza.plazoleta.application.exception.UnauthorizedException;
import com.plaza.plazoleta.domain.model.Category;
import com.plaza.plazoleta.domain.model.Dish;

import com.plaza.plazoleta.domain.model.Restaurant;
import com.plaza.plazoleta.domain.port.CategoryRepository;
import com.plaza.plazoleta.domain.port.DishRepository;
import com.plaza.plazoleta.domain.port.RestaurantRepository;
import com.plaza.plazoleta.domain.port.UserServiceClient;
import com.plaza.plazoleta.domain.service.DishService;
import com.plaza.plazoleta.infraestructure.config.JwtUtils;
import com.plaza.plazoleta.infraestructure.dto.StatusDishDto;
import com.plaza.plazoleta.infraestructure.dto.UpdateDishDto;
import com.plaza.plazoleta.infraestructure.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final RestaurantRepository restaurantRepository;
    private final CategoryRepository categoryRepository;
    private final UserServiceClient userServiceClient;
    private final JwtUtils jwtUtils;

    @Autowired
    public DishServiceImpl(
            DishRepository dishRepository,
            RestaurantRepository restaurantRepository,
            CategoryRepository categoryRepository,
            UserServiceClient userServiceClient,
            JwtUtils jwtUtils
    ) {
        this.dishRepository = dishRepository;
        this.restaurantRepository = restaurantRepository;
        this.categoryRepository = categoryRepository;
        this.userServiceClient = userServiceClient;
        this.jwtUtils = jwtUtils;
    }


    @Override
    public Dish createDish(Dish dish) {

        // 1. Obtener usuario autenticado
        Long userId = getAuthenticatedUserId();

        //2. Validar que el usuario sea propietario
        validateUserIsOwner(userId);

        // 3. Obtener el restaurante asociado al propietario
        Restaurant restaurant = restaurantRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Restaurante no encontrado"));

        // 4. Validar que la categoría existe
        Long categoryId = dish.getCategory().getId();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NotFoundException("Categoría no válida"));

        // 5. Validar campos del plato
        validateDish(dish);

        // 6. Asignar relaciones y valores por defecto
        dish.setRestaurant(restaurant);
        dish.setCategory(category);
        dish.setActive(true);

        // 7. Guardar en la base de datos
        return dishRepository.save(dish);
    }

    @Override
    public Dish updateDish(Long dishId, UpdateDishDto request) {

        Long userId = getAuthenticatedUserId();

        Dish dish = validateAndGetDish(dishId, userId);

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            dish.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            validatePrice(request.getPrice()); //
            dish.setPrice(request.getPrice());
        }


        return dishRepository.save(dish);
    }

    @Override
    public Dish updateDishStatus(Long dishId, StatusDishDto statusDishDto) {

        Long userId = getAuthenticatedUserId();

        Dish dish = validateAndGetDish(dishId, userId);

        if (statusDishDto.getActive() != null) {
            dish.setActive(statusDishDto.getActive());
        }

        return dishRepository.save(dish);
    }

    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = (String) authentication.getCredentials();
        return jwtUtils.extractUserId(token.replace("Bearer ", ""));
    }

    private void validateUserIsOwner(Long userId) {
        UserDto user = userServiceClient.getUserById(userId);
        if (!"PROPIETARIO".equals(user.getRole())) {
            throw new UnauthorizedException("Acción restringida a propietarios");
        }
    }

    private void validateRestaurantOwnership(Long userId, Restaurant restaurant) {
        if (!restaurant.getUserId().equals(userId)) {
            throw new UnauthorizedException("No eres dueño de este restaurante");
        }
    }

    private Dish validateAndGetDish(Long dishId, Long userId) {
        Dish dish = dishRepository.findById(dishId)
                .orElseThrow(() -> new NotFoundException("Plato no encontrado"));
        validateUserIsOwner(userId);
        validateRestaurantOwnership(userId, dish.getRestaurant());
        return dish;
    }




    private void validateDish(Dish dish) {
        validateRequiredFields(dish);
        validatePrice(dish.getPrice());
    }

    private void validateRequiredFields(Dish dish) {
        if (dish.getName() == null || dish.getName().isBlank() ||
                dish.getPrice() == null ||
                dish.getDescription() == null || dish.getDescription().isBlank() ||
                dish.getImageUrl() == null || dish.getImageUrl().isBlank() ||
                dish.getCategory() == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
    }

    private void validatePrice(Integer price) {
        if (price == null || price <= 0) {
            throw new InvalidPriceException("El precio debe ser entero mayor a cero");
        }
    }
}
