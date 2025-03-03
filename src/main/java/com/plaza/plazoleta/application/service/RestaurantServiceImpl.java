package com.plaza.plazoleta.application.service;

import com.plaza.plazoleta.application.exception.InvalidOwnerException;
import com.plaza.plazoleta.domain.model.Restaurant;
import com.plaza.plazoleta.domain.port.RestaurantRepository;
import com.plaza.plazoleta.domain.port.UserServiceClient;
import com.plaza.plazoleta.domain.service.RestaurantService;
import com.plaza.plazoleta.infraestructure.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final UserServiceClient userServiceClient;

    @Autowired
    public RestaurantServiceImpl(
            RestaurantRepository restaurantRepository,
            UserServiceClient userServiceClient
    ) {
        this.restaurantRepository = restaurantRepository;
        this.userServiceClient = userServiceClient;
    }

    @Value("${owner.role.name}")
    private String ownerRoleName;

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {

        validateRol(restaurant);
        validateNit(restaurant);
        validateCellPhone(restaurant);
        validateName(restaurant);
        validateRequiredFields(restaurant);

        return restaurantRepository.save(restaurant);
    }

    private void validateRol(Restaurant restaurant) {

        UserDto owner = userServiceClient.getUserById(restaurant.getUserId());

        if (!owner.getRole().equals(ownerRoleName)) {
            throw new InvalidOwnerException("El usuario no tiene el rol de propietario");
        }


    }

    private void validateNit(Restaurant restaurant) {

        String nit = restaurant.getNit();

        if (nit == null || !nit.matches("^\\d+$")) {
            throw new RuntimeException("El NIT debe contener solo números");
        }
    }

    private void validateCellPhone (Restaurant restaurant) {

        String cellPhone = restaurant.getCellPhone();

        if (cellPhone == null || !cellPhone.matches("^\\+?\\d{1,13}$")) {
            throw new RuntimeException("El teléfono es requerido");
        }
    }

    private void validateName (Restaurant restaurant) {

        String name = restaurant.getName();

        if (name == null || name.matches("^\\d+$")) {
            throw new RuntimeException("El nombre es requerido");
        }
    }

    private void validateRequiredFields(Restaurant restaurant) {
        if (restaurant.getName() == null || restaurant.getName().isBlank() ||
                restaurant.getNit() == null || restaurant.getNit().isBlank() ||
                restaurant.getAddress() == null || restaurant.getAddress().isBlank() ||
                restaurant.getCellPhone() == null || restaurant.getCellPhone().isBlank() ||
                restaurant.getUrlLogo() == null || restaurant.getUrlLogo().isBlank() ||
                restaurant.getUserId() == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios: nombre, NIT, dirección, teléfono, URL del logo y ID del usuario.");
        }
    }
}



