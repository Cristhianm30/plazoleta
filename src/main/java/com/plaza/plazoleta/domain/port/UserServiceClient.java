package com.plaza.plazoleta.domain.port;

import com.plaza.plazoleta.infraestructure.config.FeignConfig;
import com.plaza.plazoleta.infraestructure.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "User-service",url = "http://localhost:8080", configuration = FeignConfig.class)
public interface UserServiceClient {

    @GetMapping("/user/{id}")
    UserDto getUserById(@PathVariable("id") Long id);

    @GetMapping("/user/email")
    UserDto getUserByEmail(@RequestParam("email") String email);

}
