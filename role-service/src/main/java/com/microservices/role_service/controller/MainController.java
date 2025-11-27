package com.microservices.role_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.role_service.entity.Role;
import com.microservices.role_service.service.Roleservice;

@RestController
@RequestMapping("/roles")
public class MainController {

    @Autowired
    private Roleservice roleimp;

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role roles) {
        Role savedRole = roleimp.insertrole(roles);
        System.out.println("Success...");
        return ResponseEntity.ok(savedRole);
    }

    @GetMapping("/name/{name}")
    public Role getRole(@PathVariable("name") String name) {
        return roleimp.getRole(name);
    }
}
