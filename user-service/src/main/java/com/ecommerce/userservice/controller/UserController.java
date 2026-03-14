package com.ecommerce.userservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.userservice.dto.LoginRequest;
import com.ecommerce.userservice.dto.LoginResponse;
import com.ecommerce.userservice.dto.RefreshRequest;
import com.ecommerce.userservice.dto.UserRequest;
import com.ecommerce.userservice.dto.UserResponse;
import com.ecommerce.userservice.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private UserService userService;

    // Constructor Injection
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

//        String token = userService.login(request.getEmail(), request.getPassword());
        LoginResponse response = userService.login(request.getEmail(), request.getPassword());

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshToken(
            @RequestBody RefreshRequest request) {

        return ResponseEntity.ok(
                userService.refreshAccessToken(request.getRefreshToken()));
    }

}
