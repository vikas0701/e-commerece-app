package com.ecommerce.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import com.ecommerce.apigateway.filter.RateLimiterFilter;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder,
                               RateLimiterFilter rateLimiterFilter) {

        return builder.routes()

                .route("user-service", r -> r.path("/api/users/**")
                        .filters(f -> f.filter(rateLimiterFilter))
                        .uri("lb://USER-SERVICE"))

                .route("product-service", r -> r.path("/api/products/**")
                        .filters(f -> f.filter(rateLimiterFilter))
                        .uri("lb://PRODUCT-SERVICE"))

                .build();
    }
}
