package com.ecommerce.paymentservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.common.events.PaymentEvent;


@Service
public class PaymentService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final PaymentProcessorService processor;
    
    public PaymentService(KafkaTemplate<String, Object> kafkaTemplate, PaymentProcessorService processor) {
        this.kafkaTemplate = kafkaTemplate;
        this.processor = processor;
    }

    @KafkaListener(topics = "payment-topic", groupId = "payment-group", concurrency = "4")
    public void listenPaymentEvent(PaymentEvent event) {

        System.out.println(
                "THREAD: " + Thread.currentThread().getName() +
                " Processing payment for order: " + event.getOrderId()
            );

        processor.processPayment(event);   // call protected method
        
    }
    
    
}

