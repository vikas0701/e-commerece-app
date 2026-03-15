package com.ecommerce.paymentservice.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.common.events.OrderEvent;
import com.ecommerce.paymentservice.entity.ProcessedEvent;
import com.ecommerce.paymentservice.repository.ProcessedEventRepository;

@Service
public class PaymentKafkaConsumer {

    private final ProcessedEventRepository processedEventRepository;

    public PaymentKafkaConsumer(ProcessedEventRepository processedEventRepository) {
        this.processedEventRepository = processedEventRepository;
    }

    @KafkaListener(topics = "order-topic", groupId = "payment-group")
    @Transactional
    public void handleOrderEvent(OrderEvent event) {

        String eventId = event.getEventId().toString();

        // Idempotency check
        if (processedEventRepository.existsById(eventId)) {
            System.out.println("Duplicate event ignored: " + eventId);
            return;
        }

        System.out.println("Processing payment for order: " + event.getOrderId());

        processedEventRepository.save(new ProcessedEvent(eventId));
    }
}