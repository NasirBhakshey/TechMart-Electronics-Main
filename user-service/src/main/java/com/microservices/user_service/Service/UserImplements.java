package com.microservices.user_service.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.microservices.dto.Roledto;
import com.microservices.user_service.Repository.UserRepository;
import com.microservices.user_service.Security.JwtService;
import com.microservices.user_service.Security.RoleClient;
import com.microservices.user_service.entities.User;

@Service
public class UserImplements implements UserInterface {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleClient client;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User InsertUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Roledto role = client.getRoleByName("USER");
            user.setRole(role.getName());
            return userRepository.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUserById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }

    @Override
    public User getUserByEmail(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserByEmail'");
    }

    @Override
    public String login(String email, String password) {

        Optional<User> optional = userRepository.findByemail(email);

        if (optional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        User user = optional.get();

        boolean isMatch = passwordEncoder.matches(password, user.getPassword());
        if (!isMatch) {
            throw new RuntimeException("Invalid Password");
        }
        List<String> roles = Arrays.asList(user.getRole().split(","));
        return jwtService.generateAccessToken(user.getName(), roles);

    }

    @Override
    public User updateUser(User user, Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean deleteuser(User user, Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteuser'");
    }

    @Override
    public String findbyrole(String role) {
        Optional<User> optional = userRepository.findByRole(role);
        if(optional.isPresent()){
            return optional.get().getRole();
        }else{
            return null;
        }
    }

}
