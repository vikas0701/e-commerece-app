//package com.ecommerce.userservice.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.ecommerce.userservice.entity.RefreshToken;
//
//public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
//
//    Optional<RefreshToken> findByToken(String token);
//
//    void deleteByUserId(Long userId);
//}