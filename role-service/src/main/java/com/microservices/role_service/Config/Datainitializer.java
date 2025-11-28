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

        if (rolerepository.count() > 0) {
            Role role = new Role();
            role.setName("ADMIN");

            Role role1 = new Role();
            role1.setName("USER");

            Role role2 = new Role();
            role2.setName("MANAGER");

            rolerepository.save(role);
            rolerepository.save(role1);
            rolerepository.save(role2);

            System.out.println("✅ Seeded default role");
        }

    }

}
