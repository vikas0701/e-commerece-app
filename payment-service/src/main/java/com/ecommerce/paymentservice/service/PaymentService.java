package com.ecommerce.paymentservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.common.events.PaymentEvent;
import com.ecommerce.paymentservice.entity.ProcessedEvent;
import com.ecommerce.paymentservice.repository.ProcessedEventRepository;


@Service
public class PaymentService {

	private final PaymentProcessorService processor;
    private final ProcessedEventRepository processedEventRepository;

    public PaymentService(PaymentProcessorService processor,
                          ProcessedEventRepository processedEventRepository) {
        this.processor = processor;
        this.processedEventRepository = processedEventRepository;
    }

    @KafkaListener(topics = "payment-topic", groupId = "payment-group", concurrency = "4")
    public void listenPaymentEvent(PaymentEvent event) {

        String eventId = event.getEventId().toString();

        if (processedEventRepository.existsById(eventId)) {
            System.out.println("Duplicate payment event ignored: " + eventId);
            return;
        }

        System.out.println(
                "THREAD: " + Thread.currentThread().getName() +
                " Processing payment for order: " + event.getOrderId()
        );

        processor.processPayment(event);

        processedEventRepository.save(new ProcessedEvent(eventId));
    }
    
    
}

