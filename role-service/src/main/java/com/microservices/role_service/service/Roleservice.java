package com.microservices.role_service.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microservices.role_service.entity.Role;
import com.microservices.role_service.repository.Rolerepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class Roleservice implements Roleinterface {

    @Autowired
    private Rolerepository repository;

    @Override
    public Role insertrole(Role role) {
        return repository.save(role);
    }

    @Override
    public Role getRole(String name) {
        Optional<Role> optional = repository.findByName(name);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return null;
        }
    }

}
