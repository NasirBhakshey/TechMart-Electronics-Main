package com.microservices.user_service.Security;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.microservices.dto.Roledto;
import com.microservices.user_service.Config.FeignConfig;

import io.github.resilience4j.retry.annotation.Retry;

@FeignClient(name = "ROLE-SERVICE", configuration = FeignConfig.class)
public interface RoleClient {

    @GetMapping("/roles/name/{name}")
    @Retry(name = "roleretry")
    Roledto getRoleByName(@PathVariable("name") String name);
}
