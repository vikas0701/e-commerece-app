package com.ecommerce.orderservice.outbox.publisher;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.common.events.InventoryRestoreEvent;
import com.ecommerce.common.events.OrderEvent;
import com.ecommerce.common.events.PaymentEvent;
import com.ecommerce.orderservice.outbox.entity.OutboxEvent;
import com.ecommerce.orderservice.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

@Component
public class OutboxPublisher {

	private final OutboxRepository repository;
	private final KafkaTemplate<String, Object> orderKafkaTemplate;
	private final ObjectMapper objectMapper;
	
	public OutboxPublisher(OutboxRepository repository,
			KafkaTemplate<String, Object> orderKafkaTemplate, ObjectMapper objectMapper) {
		this.repository = repository;
		this.orderKafkaTemplate = orderKafkaTemplate;
		this.objectMapper = objectMapper;
	}

	@Scheduled(fixedDelay = 10000)
	@Transactional
	public void publishOutboxEvents() {

	    System.out.println("---- OUTBOX PUBLISHER TRIGGERED ----");

	    List<OutboxEvent> events = repository.findTop50ByStatus("PENDING");

	    if (events.isEmpty()) {
	        System.out.println("No PENDING events found.");
	        return;
	    }

	    System.out.println("Found " + events.size() + " PENDING events.");

	    for (OutboxEvent event : events) {

	        try {

	            String topic = resolveTopic(event.getEventType());

	            System.out.println("Publishing Event ID: " + event.getId());
	            System.out.println("Event Type: " + event.getEventType());
	            System.out.println("Topic: " + topic);
	            System.out.println("Payload: " + event.getPayload());

	            Object eventObject;

	            switch (event.getEventType()) {

	                case "ORDER_CREATED":
	                    eventObject = objectMapper.readValue(event.getPayload(), OrderEvent.class);
	                    break;

	                case "PAYMENT_REQUEST":
	                    eventObject = objectMapper.readValue(event.getPayload(), PaymentEvent.class);
	                    break;

	                case "INVENTORY_RESTORE":
	                    eventObject = objectMapper.readValue(event.getPayload(), InventoryRestoreEvent.class);
	                    break;

	                default:
	                    throw new IllegalArgumentException("Unknown event type");
	            }
//	            orderKafkaTemplate.send(topic, eventObject);
	            orderKafkaTemplate.send(topic, String.valueOf(event.getAggregateId()), eventObject);

	            event.setStatus("SENT");

	            System.out.println("Event ID " + event.getId() + " marked as SENT");

	        } catch (Exception e) {

	            System.out.println("ERROR publishing event ID: " + event.getId());
	            e.printStackTrace();
	        }
	    }

	    System.out.println("---- OUTBOX PUBLISHER END ----");
	}

	private String resolveTopic(String eventType) {

	    switch (eventType) {
	        case "ORDER_CREATED":
	            return "order-topic";
	        case "PAYMENT_REQUEST":
	            return "payment-topic";
	        case "INVENTORY_RESTORE":
	            return "inventory-restore-topic";
	        default:
	            throw new IllegalArgumentException("Unknown event type");
	    }
	}
}
