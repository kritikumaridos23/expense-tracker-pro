package com.kriti.expensetracker.controller;

import com.kriti.expensetracker.dto.LoginRequest;
import com.kriti.expensetracker.dto.SignupRequest;
import com.kriti.expensetracker.model.User;
import com.kriti.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            Map<String, String> res = new HashMap<>();
            res.put("message", "Email already exists");
            return ResponseEntity.badRequest().body(res);
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // simple, later we will add encryption
        
        userRepository.save(user);
        
        Map<String, Object> res = new HashMap<>();
        res.put("message", "Signup successful");
        res.put("userId", user.getId());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            Map<String, String> res = new HashMap<>();
            res.put("message", "User not found");
            return ResponseEntity.badRequest().body(res);
        }
        User user = optionalUser.get();
        if (!user.getPassword().equals(request.getPassword())) {
            Map<String, String> res = new HashMap<>();
            res.put("message", "Invalid password");
            return ResponseEntity.badRequest().body(res);
        }
        Map<String, Object> res = new HashMap<>();
        res.put("message", "Login successful");
        res.put("userId", user.getId());
        res.put("name", user.getName());
        res.put("email", user.getEmail());
        return ResponseEntity.ok(res);
    }
}
