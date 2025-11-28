package com.microservices.role_service.service;

import java.util.List;

import com.microservices.role_service.entity.Role;

public interface Roleinterface {

    public Role insertrole(Role role);

    public Role getRole(String name);

    public List<Role> getall();

}
