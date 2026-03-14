package com.ecommerce.paymentservice.service;

import java.util.Random;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.common.events.PaymentEvent;
import com.ecommerce.common.events.PaymentResponse;

@Service
public class PaymentService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "payment-topic", groupId = "payment-group")
    public void processPayment(PaymentEvent event) {
//    public boolean processPayment(PaymentEvent event) {

        System.out.println("Processing payment for order: " + event.getOrderId());

        boolean paymentSuccess = new Random().nextBoolean();

        
        if (paymentSuccess) {

            kafkaTemplate.send("payment-response-topic",new PaymentResponse(event.getOrderId(), true));

        } else {

            kafkaTemplate.send("payment-response-topic",new PaymentResponse(event.getOrderId(), false));
        }
        
//        return paymentSuccess;
    }
}
