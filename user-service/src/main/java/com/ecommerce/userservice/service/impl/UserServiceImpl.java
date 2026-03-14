package com.ecommerce.userservice.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import com.ecommerce.userservice.dto.LoginResponse;
import com.ecommerce.userservice.dto.UserRequest;
import com.ecommerce.userservice.dto.UserResponse;
//import com.ecommerce.userservice.entity.RefreshToken;
import com.ecommerce.userservice.entity.User;
//import com.ecommerce.userservice.repository.RefreshTokenRepository;
import com.ecommerce.userservice.repository.UserRepository;
import com.ecommerce.userservice.security.JwtUtil;
import com.ecommerce.userservice.service.RefreshTokenRedisService;
import com.ecommerce.userservice.service.UserService;


@Service
public class UserServiceImpl implements UserService{

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;
	//	private final RefreshTokenRepository refreshTokenRepository;
	private final RefreshTokenRedisService refreshTokenRedisService;

	@Value("${jwt.refresh-expiration}")
	private long refreshExpiration;


	//	public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
	//		this.userRepository = userRepository;
	//		this.jwtUtil = jwtUtil;
	//		this.refreshTokenRepository = refreshTokenRepository;
	//	}

	public UserServiceImpl(UserRepository userRepository,
			JwtUtil jwtUtil,
			RefreshTokenRedisService refreshTokenRedisService) {

		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
		this.refreshTokenRedisService = refreshTokenRedisService;
	}

	@Override
	public UserResponse createUser(UserRequest request) {
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setRole("USER");

		User savedUser = userRepository.save(user);

		return mapToResponse(savedUser);
	}

	@Override
	public List<UserResponse> getAllUsers() {
		return userRepository.findAll()
				.stream()
				.map(user -> mapToResponse(user))
				.collect(Collectors.toList());
	}

	@Override
	public UserResponse getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));

		return mapToResponse(user);
	}

	private UserResponse mapToResponse(User user) {
		return new UserResponse(
				user.getId(),
				user.getName(),
				user.getEmail()
				);
	}


	@Override
	public LoginResponse login(String email, String password) {

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("Invalid credentials"));

		if (!user.getPassword().equals(password)) {
			throw new RuntimeException("Invalid credentials");
		}

		String accessToken = jwtUtil.generateToken(user.getEmail(), user.getRole());
		String refreshTokenString = jwtUtil.generateRefreshToken();

//		RefreshToken refreshToken = new RefreshToken();
//		refreshToken.setToken(refreshTokenString);
//		refreshToken.setUser(user);
//		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
//
//		refreshTokenRepository.save(refreshToken);

		refreshTokenRedisService.saveRefreshToken(user.getId(), refreshTokenString);
		
		return new LoginResponse(accessToken, refreshTokenString);
	}

	@Override
	public LoginResponse refreshAccessToken(String refreshToken) {

//		RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
//				.orElseThrow(() -> new RuntimeException("Invalid refresh token"));
//
//		if (token.getExpiryDate().isBefore(Instant.now())) {
//			refreshTokenRepository.delete(token);
//			throw new RuntimeException("Refresh token expired");
//		}
//
//		User user = token.getUser();

		String userId = refreshTokenRedisService.getUserId(refreshToken);

	    if (userId == null) {
	        throw new RuntimeException("Invalid refresh token");
	    }
	    
	    // delete old refresh token
	    refreshTokenRedisService.deleteToken(refreshToken);
	    
	    User user = userRepository.findById(Long.parseLong(userId))
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    
	 // generate new tokens
	    String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole());
	    String newRefreshToken = jwtUtil.generateRefreshToken();
	    
	 // store new refresh token
	    refreshTokenRedisService.saveRefreshToken(user.getId(), newRefreshToken);

		return new LoginResponse(newAccessToken, refreshToken);
	}


	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
}
