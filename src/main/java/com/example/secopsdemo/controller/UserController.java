package com.example.secopsdemo.controller;

import com.example.secopsdemo.model.User;
import com.example.secopsdemo.repository.UserRepository;
import com.example.secopsdemo.service.AuthService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository userRepository;
    private final AuthService authService;

    public UserController(UserRepository userRepository, AuthService authService) {
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @GetMapping("/search")
    public List<User> search(@RequestParam String username) {
        return userRepository.findByUsernameUnsafe(username);
    }

    @PostMapping("/role")
    public Map<String, Object> updateRole(@RequestParam String username, @RequestParam String role) {
        int updated = userRepository.updateRoleUnsafe(username, role);
        Map<String, Object> response = new HashMap<>();
        response.put("updated", updated);
        return response;
    }

    @PostMapping("/token")
    public Map<String, String> token(@RequestParam String username, @RequestParam String password) {
        // VULN: Password is hashed with MD5 and no real credential validation is performed.
        Map<String, String> response = new HashMap<>();
        response.put("username", username);
        response.put("passwordHash", authService.weakHashPassword(password));
        response.put("token", authService.createToken(username));
        return response;
    }
}
