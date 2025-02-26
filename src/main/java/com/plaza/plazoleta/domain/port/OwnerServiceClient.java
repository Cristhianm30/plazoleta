package com.plaza.plazoleta.domain.port;

import com.plaza.plazoleta.infraestructure.config.FeignConfig;
import com.plaza.plazoleta.infraestructure.dto.OwnerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Owner-service",url = "http://localhost:8080", configuration = FeignConfig.class)
public interface OwnerServiceClient {

    @GetMapping("/owner/{id}")
    OwnerDto getOwnerById(@PathVariable("id") Long id);
}
