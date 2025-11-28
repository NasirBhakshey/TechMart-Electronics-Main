package com.microservices.user_service.Service;

import java.util.List;

import com.microservices.user_service.entities.User;

public interface UserInterface {

    public User InsertUser(User user);

    public User getUserById(Integer id);

    public User getUserByEmail(String email);

    public String login(String email, String password);

    public String findbyrole(String role);

    public User updateUser(User user, Integer id);

    public List<User> getAllUsers();

    public boolean deleteuser(User user, Integer id);

}
