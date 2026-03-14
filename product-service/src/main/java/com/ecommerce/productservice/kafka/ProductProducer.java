package com.ecommerce.productservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.common.events.ProductEvent;

@Service
public class ProductProducer {

    private KafkaTemplate<String, ProductEvent> kafkaTemplate;

    public ProductProducer(KafkaTemplate<String, ProductEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendProductEvent(ProductEvent event) {
        kafkaTemplate.send("product-topic", event);
    }
}
