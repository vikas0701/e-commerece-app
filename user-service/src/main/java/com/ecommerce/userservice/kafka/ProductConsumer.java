package com.ecommerce.userservice.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ecommerce.userservice.dto.ProductEvent;


@Service
public class ProductConsumer {

    @KafkaListener(topics = "product-topic", groupId = "user-group")
    public void consume(ProductEvent event) {

        System.out.println("Received Product Event:");
        System.out.println("Product ID: " + event.getId());
        System.out.println("Product Name: " + event.getName());
        System.out.println("Product Price: " + event.getPrice());
    }
}
