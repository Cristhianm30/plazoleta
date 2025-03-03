package com.plaza.plazoleta.application.service;

import com.plaza.plazoleta.domain.port.UserServiceClient;
import com.plaza.plazoleta.infraestructure.dto.UserDto;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserServiceClient userServiceClient;

    public UserDetailsServiceImpl(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Obtener UserDto desde el microservicio de usuarios usando Feign
        UserDto userDto = userServiceClient.getUserByEmail(email);

        if (userDto == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }

        // Convertir UserDto a UserDetails de Spring Security
        return User.withUsername(userDto.getEmail())
                .password(userDto.getPassword())
                .roles(userDto.getRole())
                .build();
    }
}
