package com.microservices.user_service.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.user_service.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByemail(String email);

    public Optional<User> findByRole(String role);

}
