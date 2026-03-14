package com.ecommerce.orderservice.entity;

public enum OrderStatus {
    CREATED,
    INVENTORY_CONFIRMED,
    PAYMENT_PENDING,
    COMPLETED,
    CANCELLED,
    FAILED
}