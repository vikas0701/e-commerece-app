package com.ecommerce.orderservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Component
public class ProductClient {

    @Autowired
    private RestTemplate restTemplate;

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackProduct")
    @Retry(name = "productService")
    public Object getProduct(Long productId) {

        String productUrl = "http://PRODUCT-SERVICE/api/products/" + productId;
        return restTemplate.getForObject(productUrl, Object.class);
    }

    public Object fallbackProduct(Long productId, Exception ex) {

        System.out.println("⚠ Circuit Breaker triggered - Product Service unavailable");

        return null;
    }
}
