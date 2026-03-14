package com.ecommerce.userservice.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenRedisService {

	@Autowired
    private RedisTemplate<String, String> redisTemplate;

	public void saveRefreshToken(Long userId, String token) {

	    String key = "refresh:" + token;

	    redisTemplate.opsForValue()
	                 .set(key, userId.toString(), Duration.ofDays(7));
	}

	public String getRefreshToken(Long userId) {

        String key = "refresh:" + userId ;

        return (String) redisTemplate.opsForValue().get(key);
    }
	
	public String getUserId(String token) {

        String key = "refresh:" + token;

        return (String) redisTemplate.opsForValue().get(key);
    }
	
	public void deleteToken(String token) {

	    String key = "refresh:" + token;

	    redisTemplate.delete(key);
	}
}
