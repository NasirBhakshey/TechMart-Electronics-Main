package com.microservices.role_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.role_service.entity.Role;

public interface Rolerepository extends JpaRepository<Role, Integer>{

}
