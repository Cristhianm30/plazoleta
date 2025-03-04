package com.plaza.plazoleta.application.service;

import com.plaza.plazoleta.DishDataProvider;
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
import com.plaza.plazoleta.infraestructure.config.JwtUtils;
import com.plaza.plazoleta.infraestructure.dto.UpdateDishDto;
import com.plaza.plazoleta.infraestructure.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DishServiceImplTest {

    @Mock private DishRepository dishRepository;
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private UserServiceClient userServiceClient;
    @Mock private JwtUtils jwtUtils;
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;

    @InjectMocks private DishServiceImpl dishService;

    private final Long USER_ID = 1L;
    private final String TOKEN = "";

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getCredentials()).thenReturn("Bearer " + TOKEN);
        when(jwtUtils.extractUserId(TOKEN)).thenReturn(USER_ID);
    }

    @Test
    void createDish_Success() {
        // Configurar mocks
        Dish dish = DishDataProvider.validDishMock();
        UserDto user = new UserDto();
        user.setRole("PROPIETARIO");

        when(userServiceClient.getUserById(USER_ID)).thenReturn(user);
        when(restaurantRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(DishDataProvider.validRestaurantMock()));
        when(categoryRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(DishDataProvider.validCategoryMock()));
        when(dishRepository.save(any(Dish.class))).thenReturn(dish);

        // Ejecutar
        Dish result = dishService.createDish(dish);

        // Verificar
        assertNotNull(result);
        assertEquals("Pizza Margarita", result.getName());
        verify(dishRepository).save(dish);
    }

    @Test
    void createDish_UserNotOwner() {

        UserDto user = new UserDto();
        user.setRole("CLIENTE"); // Rol inválido

        when(userServiceClient.getUserById(USER_ID)).thenReturn(user);

        assertThrows(UnauthorizedException.class,
                () -> dishService.createDish(DishDataProvider.validDishMock()));
    }

    @Test
    void createDish_RestaurantNotFound() {

        UserDto user = new UserDto();
        user.setRole("PROPIETARIO");

        when(userServiceClient.getUserById(USER_ID)).thenReturn(user);
        when(restaurantRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> dishService.createDish(DishDataProvider.validDishMock()));
    }

    @Test
    void createDish_InvalidCategory() {

        UserDto user = new UserDto();
        user.setRole("PROPIETARIO");

        when(userServiceClient.getUserById(USER_ID)).thenReturn(user);
        when(restaurantRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(DishDataProvider.validRestaurantMock()));
        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> dishService.createDish(DishDataProvider.validDishMock()));
    }


    @Test
    void createDish_InvalidPrice() {

        UserDto user = new UserDto();
        user.setRole("PROPIETARIO");
        when(userServiceClient.getUserById(USER_ID)).thenReturn(user);
        when(restaurantRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(DishDataProvider.validRestaurantMock()));
        when(categoryRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(DishDataProvider.validCategoryMock()));

        Dish dish = DishDataProvider.invalidPriceDishMock();

        assertThrows(InvalidPriceException.class,
                () -> dishService.createDish(dish));
    }

    // Pruebas adicionales para campos individuales faltantes
    @Test
    void createDish_MissingName() {

        UserDto user = new UserDto();
        user.setRole("PROPIETARIO");
        when(userServiceClient.getUserById(USER_ID)).thenReturn(user);
        when(restaurantRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(DishDataProvider.validRestaurantMock()));
        when(categoryRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(DishDataProvider.validCategoryMock()));

        Dish dish = DishDataProvider.missingNameDishMock();

        assertThrows(IllegalArgumentException.class,
                () -> dishService.createDish(dish));
    }

    @Test
    void createDish_MissingDescription() {

        UserDto user = new UserDto();
        user.setRole("PROPIETARIO");
        when(userServiceClient.getUserById(USER_ID)).thenReturn(user);
        when(restaurantRepository.findByUserId(USER_ID))
                .thenReturn(Optional.of(DishDataProvider.validRestaurantMock()));
        when(categoryRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(DishDataProvider.validCategoryMock()));

        Dish dish = DishDataProvider.missingDescriptionDishMock();

        assertThrows(IllegalArgumentException.class,
                () -> dishService.createDish(dish));
    }

    @Test
    void updateDish_Success() {
        // Configurar mocks
        UpdateDishDto request = new UpdateDishDto();
        request.setDescription("Nueva descripción");
        request.setPrice(2000);

        Dish existingDish = DishDataProvider.validDishMock();
        UserDto user = new UserDto();
        user.setRole("PROPIETARIO");
        Restaurant restaurant = existingDish.getRestaurant();
        restaurant.setUserId(USER_ID); // Asegurar coincidencia de IDs

        when(dishRepository.findById(1L)).thenReturn(Optional.of(existingDish));
        when(userServiceClient.getUserById(USER_ID)).thenReturn(user);
        when(dishRepository.save(any(Dish.class))).thenReturn(existingDish);

        // Ejecutar
        Dish result = dishService.updateDish(1L, request);

        // Verificar
        assertEquals("Nueva descripción", result.getDescription());
        assertEquals(2000, result.getPrice());
        verify(dishRepository).save(existingDish);
    }
}