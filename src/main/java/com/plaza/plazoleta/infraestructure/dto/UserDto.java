package com.plaza.plazoleta.infraestructure.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String lastName;
    private String password;
    private String documentNumber;
    private String cellPhone;
    private LocalDate birthDate;
    private String email;
    private String role;
}
