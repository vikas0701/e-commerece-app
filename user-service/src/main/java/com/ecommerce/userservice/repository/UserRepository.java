package com.ecommerce.userservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.userservice.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	 // Custom query to find user by email
    Optional<User> findByEmail(String email);
    
}
