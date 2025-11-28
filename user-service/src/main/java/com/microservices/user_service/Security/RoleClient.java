package com.microservices.user_service.Security;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.microservices.dto.Roledto;

@FeignClient(name = "role-service")
public interface RoleClient {

    @GetMapping("/roles/name/{name}")
    Roledto getRoleByName(@PathVariable("name") String name);
}
