package com.microservices.user_service.MainController;

import org.springframework.web.bind.annotation.RestController;

import com.microservices.user_service.Service.UserImplements;
import com.microservices.user_service.entities.User;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/auth")
public class user_controller {

    @Autowired
    private UserImplements implements1;

    @PostMapping("/register")
    public Map<String,String> register(@RequestBody User user){
        User user2 = implements1.InsertUser(user);
        String token =user2.toString();
        return Map.of("token", token);
    }

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody Map<String,String> payload){
        String token = implements1.login(payload.get("username"), payload.get("password"));
        return Map.of("token", token);
    }

}
