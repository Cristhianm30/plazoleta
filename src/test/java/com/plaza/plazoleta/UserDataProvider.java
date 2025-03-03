package com.plaza.plazoleta;

import com.plaza.plazoleta.infraestructure.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

public class UserDataProvider {

    public static UserDto validOwnerMock() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John");
        userDto.setLastName("Doe");
        userDto.setDocumentNumber("1234567890");
        userDto.setCellPhone("+573001234567");
        userDto.setBirthDate(LocalDate.now().minusYears(20));
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password123");
        userDto.setRole("PROPIETARIO");
        return userDto;
    }

    public static UserDto invalidOwnerMock() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("John");
        userDto.setLastName("Doe");
        userDto.setDocumentNumber("1234567890");
        userDto.setCellPhone("+573001234567");
        userDto.setBirthDate(LocalDate.now().minusYears(20));
        userDto.setEmail("john.doe@example.com");
        userDto.setPassword("password123");
        userDto.setRole("CLIENTE");
        return userDto;
    }



}
