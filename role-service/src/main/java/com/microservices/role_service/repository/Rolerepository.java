package com.microservices.role_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.role_service.entity.Role;
import java.util.Optional;


public interface Rolerepository extends JpaRepository<Role, Integer>{

    public Optional<Role> findByName(String name);

}
