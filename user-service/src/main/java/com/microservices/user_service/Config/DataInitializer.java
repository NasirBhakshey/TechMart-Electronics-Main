package com.microservices.user_service.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.microservices.dto.Roledto;
import com.microservices.user_service.Repository.UserRepository;
import com.microservices.user_service.Security.RoleClient;
import com.microservices.user_service.entities.User;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleClient client;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.count() == 0) {

            Roledto adminRole = client.getRoleByName("ADMIN");
            Roledto managerRole = client.getRoleByName("MANAGER");
            Roledto userRole = client.getRoleByName("USER");

            User admin = new User();
            admin.setName("ADMIN");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin@1234"));
            admin.setRole(adminRole.getName());

            User manager = new User();
            manager.setName("MANAGER");
            manager.setEmail("manager@example.com");
            manager.setPassword(passwordEncoder.encode("MANAGER@1234"));
            manager.setRole(managerRole.getName());

            User user = new User();
            user.setName("USER");
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("USER@1234"));
            user.setRole(userRole.getName());

            userRepository.save(admin);
            userRepository.save(manager);
            userRepository.save(user);

            System.out.println("✅ Seeded default users");
        }
    }

}
