package com.ecommerce.userservice.service;

import java.util.List;

import com.ecommerce.userservice.dto.LoginResponse;
import com.ecommerce.userservice.dto.UserRequest;
import com.ecommerce.userservice.dto.UserResponse;

public interface UserService {

	UserResponse createUser(UserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);
    
    LoginResponse login(String email, String password);
    
    void deleteUser(Long id);
    public LoginResponse refreshAccessToken(String refreshToken);
}
