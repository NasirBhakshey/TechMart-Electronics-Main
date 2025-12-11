package com.microservices.user_service.MainController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.microservices.dto.LoginRequest;
import com.microservices.dto.LoginResponse;
import com.microservices.user_service.Repository.UserRepository;
import com.microservices.user_service.Service.UserImplements;
import com.microservices.user_service.entities.User;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/auth")
public class user_controller {

    @Autowired
    private UserImplements userImplements;

    @Autowired
    private UserRepository userRepository;

    private ResponseEntity<?> processUserRegistration(User user) {
        Optional<User> existingUser = userRepository.findByemail(user.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("This Email-ID is already registered...");
        }
        try {
            User savedUser = userImplements.InsertUser(user);
            return ResponseEntity.status(HttpStatus.OK).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering user: " + e.getMessage());
        }
    }

    @PostMapping(value = "/createuser")
    public ModelAndView registerUserForm(@ModelAttribute("user") User user, RedirectAttributes redirectAttributes) {
        ResponseEntity<?> response = processUserRegistration(user);
        if (response.getStatusCode() == HttpStatus.OK) {
            redirectAttributes.addFlashAttribute("successmsg", "User registered successfully. Please log in.");
            return new ModelAndView("redirect:/api/user/loginpage");
        } else {
            redirectAttributes.addFlashAttribute("errormsg", response.getBody());
            return new ModelAndView("redirect:/api/user/reg-page");
        }
    }

    @PostMapping(value = "/create-user")
    public ResponseEntity<?> registerUser(@RequestBody User user, RedirectAttributes redirectAttributes) {
        ResponseEntity<?> response = processUserRegistration(user);
        if (response.getStatusCode() == HttpStatus.OK) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body("Field while registring....");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userImplements.getAllUsers());
    }
    
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userImplements.login(request.getEmail(), request.getPassword());
    }

}
