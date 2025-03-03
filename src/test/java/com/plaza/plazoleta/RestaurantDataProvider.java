package com.plaza.plazoleta;

import com.plaza.plazoleta.domain.model.Restaurant;

public class RestaurantDataProvider {

    public static Restaurant validRestaurantMock() {
        return Restaurant.builder()
                .name("Restaurant A")
                .nit("1234567890")
                .address("Calle 123")
                .cellPhone("+573001234567")
                .urlLogo("http://example.com/logo.png")
                .userId(1L)
                .build();
    }

    public static Restaurant invalidNameRestaurantMock() {
        return Restaurant.builder()
                .name("12345") // Nombre inválido (solo números)
                .nit("1234567890")
                .address("Calle 123")
                .cellPhone("+573001234567")
                .urlLogo("http://example.com/logo.png")
                .userId(1L)
                .build();
    }

    public static Restaurant invalidNitRestaurantMock() {
        return Restaurant.builder()
                .name("Restaurant A")
                .nit("invalid_nit") // NIT inválido (no numérico)
                .address("Calle 123")
                .cellPhone("+573001234567")
                .urlLogo("http://example.com/logo.png")
                .userId(1L)
                .build();
    }

    public static Restaurant invalidCellPhoneRestaurantMock() {
        return Restaurant.builder()
                .name("Restaurant A")
                .nit("1234567890")
                .address("Calle 123")
                .cellPhone("+57300123456789") // Teléfono inválido (más de 13 caracteres)
                .urlLogo("http://example.com/logo.png")
                .userId(1L)
                .build();
    }

    public static Restaurant missingRequiredFieldsRestaurantMock() {
        return Restaurant.builder()
                .name("Restaurant A")
                .nit("1234567890")
                .cellPhone("+573001234567")
                .urlLogo("http://example.com/logo.png")
                .userId(1L)
                .build(); // Falta la dirección
    }
}