package com.ecommerce.paymentservice.service;

import java.time.LocalTime;
import java.util.Random;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.common.events.PaymentEvent;
import com.ecommerce.common.events.PaymentResponse;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;

@Service
public class PaymentProcessorService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public PaymentProcessorService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Bulkhead(name = "paymentBulkhead", type = Type.SEMAPHORE, fallbackMethod = "paymentFallback")
    public void processPayment(PaymentEvent event) {

    	System.out.println(LocalTime.now() + " ENTER BULKHEAD: " + event.getOrderId());
    	
//        try {
//            Thread.sleep(30000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        boolean paymentSuccess = new Random().nextBoolean();

        kafkaTemplate.send(
                "payment-response-topic",
                new PaymentResponse(event.getOrderId(), paymentSuccess)
        );
        
        System.out.println(LocalTime.now() + " EXIT BULKHEAD: " + event.getOrderId());
        
    }

    public void paymentFallback(PaymentEvent event, Throwable ex) {

        System.out.println("BULKHEAD FULL -> FALLBACK for order: " + event.getOrderId());
        
        kafkaTemplate.send(
                "payment-response-topic",
                new PaymentResponse(event.getOrderId(), false)
        );
    }
}