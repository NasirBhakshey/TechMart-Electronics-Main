package com.microservices.role_service.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.microservices.role_service.entity.Role;
import com.microservices.role_service.repository.Rolerepository;

@Component
public class Datainitializer implements CommandLineRunner {

    @Autowired
    Rolerepository rolerepository;

    @Override
    public void run(String... args) throws Exception {

        if (!rolerepository.existsByName("ADMIN")) {
            rolerepository.save(new Role(null, "ADMIN"));
        }
        if (!rolerepository.existsByName("USER")) {
            rolerepository.save(new Role(null, "USER"));
        }
        if (!rolerepository.existsByName("MANAGER")) {
            rolerepository.save(new Role(null, "MANAGER"));
        }

        System.out.println("Default roles inserted in role-service.");

    }

}
