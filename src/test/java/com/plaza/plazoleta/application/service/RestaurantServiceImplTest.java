package com.plaza.plazoleta.application.service;

import com.plaza.plazoleta.RestaurantDataProvider;
import com.plaza.plazoleta.UserDataProvider;
import com.plaza.plazoleta.application.exception.InvalidOwnerException;
import com.plaza.plazoleta.domain.model.Restaurant;
import com.plaza.plazoleta.domain.port.UserServiceClient;
import com.plaza.plazoleta.domain.port.RestaurantRepository;
import com.plaza.plazoleta.infraestructure.config.JwtUtils;
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
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceImplTest {
    @Mock private RestaurantRepository restaurantRepository;
    @Mock private UserServiceClient userServiceClient;
    @Mock private JwtUtils jwtUtils;
    @Mock private SecurityContext securityContext;
    @Mock private Authentication authentication;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private final Long USER_ID = 1L;
    private final String TOKEN = "";

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getCredentials()).thenReturn("Bearer " + TOKEN);
        when(jwtUtils.extractUserId(TOKEN)).thenReturn(USER_ID);
        ReflectionTestUtils.setField(restaurantService, "ownerRoleName", "PROPIETARIO");
        UserDto adminUser = new UserDto();
        adminUser.setRole("ADMINISTRADOR");
        when(userServiceClient.getUserById(USER_ID)).thenReturn(adminUser);
    }

    @Test
    public void testCreateRestaurant() {
        // Given
        Restaurant restaurant = RestaurantDataProvider.validRestaurantMock();
        UserDto adminUser = UserDataProvider.validAdminMock();
        adminUser.setRole("ADMINISTRADOR");

        // Mock del PROPIETARIO asociado al restaurante
        UserDto ownerUser = new UserDto();
        ownerUser.setRole("PROPIETARIO");
        when(userServiceClient.getUserById(2L)).thenReturn(ownerUser);


        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);

        // When
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);

        // Then
        assertNotNull(createdRestaurant);
        assertEquals("Restaurant A", createdRestaurant.getName());
        assertEquals("1234567890", createdRestaurant.getNit());
        assertEquals("+573001234567", createdRestaurant.getCellPhone());
        assertEquals(2L, createdRestaurant.getUserId());
        assertEquals("Calle 123", createdRestaurant.getAddress());
        assertEquals("http://example.com/logo.png", createdRestaurant.getUrlLogo());
        assertEquals("ADMINISTRADOR", adminUser.getRole());
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
        when(userServiceClient.getUserById(2L)).thenReturn(userDto);


        // When & Then
        assertThrows(IllegalArgumentException.class, () -> restaurantService.createRestaurant(restaurant));
    }

    @Test
    public void testCreateRestaurant_invalidOwner() {
        // Given
        Restaurant restaurant = RestaurantDataProvider.validRestaurantMock();
        UserDto userDto = UserDataProvider.validClientMock();
        when(userServiceClient.getUserById(2L)).thenReturn(userDto);

        // When & Then
        assertThrows(InvalidOwnerException.class, () -> restaurantService.createRestaurant(restaurant));
    }
}