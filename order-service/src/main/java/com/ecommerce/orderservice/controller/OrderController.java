package com.ecommerce.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order createOrder(@RequestParam Long userId,
                             @RequestParam Long productId,
                             @RequestParam Integer quantity) {
        return orderService.placeOrder(userId, productId, quantity);
    }
}

