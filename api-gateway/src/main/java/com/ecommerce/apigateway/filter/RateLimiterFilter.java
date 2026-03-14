package com.ecommerce.apigateway.filter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

@Component
public class RateLimiterFilter implements GatewayFilter {

    private final RateLimiter rateLimiter;

    public RateLimiterFilter(RateLimiterRegistry rateLimiterRegistry) {
        this.rateLimiter = rateLimiterRegistry.rateLimiter("apiLimiter");
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {

        if (!rateLimiter.acquirePermission()) {

            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);

            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }
}