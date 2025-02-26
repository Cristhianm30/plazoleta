package com.plaza.plazoleta.application.service;

import com.plaza.plazoleta.application.exception.InvalidOwnerException;
import com.plaza.plazoleta.domain.model.Restaurant;
import com.plaza.plazoleta.domain.port.RestaurantRepository;
import com.plaza.plazoleta.domain.port.OwnerServiceClient;
import com.plaza.plazoleta.domain.service.RestaurantService;
import com.plaza.plazoleta.infraestructure.dto.OwnerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private OwnerServiceClient ownerServiceClient;

    @Value("${owner.role.id}")
    private Long ownerRoleId;

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {

        validateRol_Id(restaurant);
        validateNit(restaurant);
        validateCellPhone(restaurant);
        validateName(restaurant);

        return restaurantRepository.save(restaurant);
    }

    private void validateRol_Id(Restaurant restaurant) {

        OwnerDto owner = ownerServiceClient.getOwnerById(restaurant.getUserId());

        if (!owner.getRole().equals(ownerRoleId)) {
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
}



