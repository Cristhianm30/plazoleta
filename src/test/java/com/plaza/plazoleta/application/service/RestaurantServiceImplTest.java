package com.plaza.plazoleta.application.service;

import com.plaza.plazoleta.RestaurantDataProvider;
import com.plaza.plazoleta.UserDataProvider;
import com.plaza.plazoleta.application.exception.InvalidOwnerException;
import com.plaza.plazoleta.domain.model.Restaurant;
import com.plaza.plazoleta.domain.port.UserServiceClient;
import com.plaza.plazoleta.domain.port.RestaurantRepository;
import com.plaza.plazoleta.infraestructure.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {
    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(restaurantService, "ownerRoleName", "PROPIETARIO");
    }

    @Test
    public void testCreateRestaurant() {
        // Given
        Restaurant restaurant = RestaurantDataProvider.validRestaurantMock();
        UserDto userDto = UserDataProvider.validOwnerMock();

        when(userServiceClient.getUserById(1L)).thenReturn(userDto);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        // When
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);

        // Then
        assertNotNull(createdRestaurant);
        assertEquals("Restaurant A", createdRestaurant.getName());
        assertEquals("1234567890", createdRestaurant.getNit());
        assertEquals("+573001234567", createdRestaurant.getCellPhone());
        assertEquals(1L, createdRestaurant.getUserId());
        assertEquals("Calle 123", createdRestaurant.getAddress());
        assertEquals("http://example.com/logo.png", createdRestaurant.getUrlLogo());
        assertEquals("PROPIETARIO", userDto.getRole());
    }

    @Test
    public void testCreateRestaurant_invalidName() {
        // Given
        Restaurant restaurant = RestaurantDataProvider.invalidNameRestaurantMock();

        // When & Then
        assertThrows(RuntimeException.class, () -> restaurantService.createRestaurant(restaurant));
    }

    @Test
    public void testCreateRestaurant_invalidNit() {
        // Given
        Restaurant restaurant = RestaurantDataProvider.invalidNitRestaurantMock();

        // When & Then
        assertThrows(RuntimeException.class, () -> restaurantService.createRestaurant(restaurant));
    }

    @Test
    public void testCreateRestaurant_invalidCellPhone() {
        // Given
        Restaurant restaurant = RestaurantDataProvider.invalidCellPhoneRestaurantMock();

        // When & Then
        assertThrows(RuntimeException.class, () -> restaurantService.createRestaurant(restaurant));
    }

    @Test
    public void testCreateRestaurant_missingRequiredFields() {
        // Given
        Restaurant restaurant = RestaurantDataProvider.missingRequiredFieldsRestaurantMock();

        UserDto userDto = UserDataProvider.validOwnerMock();
        when(userServiceClient.getUserById(1L)).thenReturn(userDto);


        // When & Then
        assertThrows(IllegalArgumentException.class, () -> restaurantService.createRestaurant(restaurant));
    }

    @Test
    public void testCreateRestaurant_invalidOwner() {
        // Given
        Restaurant restaurant = RestaurantDataProvider.validRestaurantMock();
        UserDto userDto = UserDataProvider.invalidOwnerMock();
        when(userServiceClient.getUserById(1L)).thenReturn(userDto);

        // When & Then
        assertThrows(InvalidOwnerException.class, () -> restaurantService.createRestaurant(restaurant));
    }
}