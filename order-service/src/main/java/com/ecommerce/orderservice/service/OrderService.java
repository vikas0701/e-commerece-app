package com.ecommerce.orderservice.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ecommerce.common.events.InventoryEvent;
import com.ecommerce.common.events.InventoryRestoreEvent;
import com.ecommerce.common.events.OrderEvent;
import com.ecommerce.common.events.PaymentEvent;
import com.ecommerce.common.events.PaymentResponse;
import com.ecommerce.orderservice.client.ProductClient;
//import com.ecommerce.orderservice.dto.InventoryEvent;
//import com.ecommerce.orderservice.dto.InventoryRestoreEvent;
//import com.ecommerce.orderservice.dto.OrderEvent;
//import com.ecommerce.orderservice.dto.PaymentEvent;
//import com.ecommerce.orderservice.dto.PaymentResponse;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.OrderStatus;
import com.ecommerce.orderservice.outbox.entity.OutboxEvent;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.repository.OutboxRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.transaction.Transactional;


@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;


	//	@Autowired
	private ProductClient productClient;

	private final OutboxRepository outboxRepository;
	private final ObjectMapper objectMapper;

	public OrderService(OrderRepository orderRepository,
			ProductClient productClient,
			OutboxRepository outboxRepository,
			ObjectMapper objectMapper) {

		this.orderRepository = orderRepository;
		this.productClient = productClient;
		this.outboxRepository = outboxRepository;
		this.objectMapper = objectMapper;
	}

	@Transactional
	public Order placeOrder(Long userId, Long productId, Integer quantity) {

	    System.out.println("=== PLACE ORDER STARTED ===");
	    System.out.println("UserId: " + userId + ", ProductId: " + productId + ", Quantity: " + quantity);

	    Object product = productClient.getProduct(productId);
	    System.out.println("Product response: " + product);

	    if (product == null) {
	        System.out.println("Product NOT found. Creating FAILED order.");

	        Order failedOrder = new Order(userId, productId, quantity, 0.0, OrderStatus.FAILED);
	        orderRepository.save(failedOrder);

	        System.out.println("FAILED Order saved with ID: " + failedOrder.getId());
	        System.out.println("=== PLACE ORDER END ===");

	        return failedOrder;
	    }

	    Order order = new Order(userId, productId, quantity, 100.0, OrderStatus.CREATED);
	    orderRepository.save(order);

	    System.out.println("Order saved in DB with ID: " + order.getId());

	    OrderEvent event = new OrderEvent(
	            UUID.randomUUID(),
	            order.getId(),
	            order.getUserId(),
	            order.getProductId(),
	            order.getQuantity()
	    );

	    try {
	        String payload = objectMapper.writeValueAsString(event);

	        OutboxEvent outbox = new OutboxEvent();
	        outbox.setAggregateId(order.getId());
	        outbox.setAggregateType("ORDER");
	        outbox.setEventType("ORDER_CREATED");
	        outbox.setPayload(payload);
	        outbox.setStatus("PENDING");

	        outboxRepository.save(outbox);

	        System.out.println("Outbox event SAVED with status PENDING for orderId: " + order.getId());

	    } catch (Exception e) {
	        System.out.println("ERROR while serializing event");
	        e.printStackTrace();
	        throw new RuntimeException(e);
	    }

	    System.out.println("=== PLACE ORDER END ===");

	    return order;
	}

	@KafkaListener(topics = "inventory-topic", groupId = "order-group")
	public void updateOrderAfterInventory(InventoryEvent event) {

		System.out.println("=== INVENTORY EVENT RECEIVED ===");
	    System.out.println("OrderId: " + event.getOrderId());
	    System.out.println("Status: " + event.getStatus());

		Order order = orderRepository.findById(event.getOrderId()).orElse(null);
		if (order == null) {
	        System.out.println("Order not found for inventory event.");
	        return;
	    }

		if ("INVENTORY_CONFIRMED".equals(event.getStatus())) {

			order.setStatus(OrderStatus.PAYMENT_PENDING);
			orderRepository.save(order);

	        System.out.println("Order status updated to PAYMENT_PENDING for orderId: " + order.getId());

			try {
			    PaymentEvent paymentEvent = new PaymentEvent(
			            order.getId(),
			            order.getUserId(),
			            order.getTotalPrice()
			    );

			    String payload = objectMapper.writeValueAsString(paymentEvent);

			    OutboxEvent outbox = new OutboxEvent();
			    outbox.setAggregateId(order.getId());
			    outbox.setAggregateType("ORDER");
			    outbox.setEventType("PAYMENT_REQUEST");
			    outbox.setPayload(payload);
			    outbox.setStatus("PENDING");

			    System.out.println("Payment event saved to OUTBOX as PENDING");

			    outboxRepository.save(outbox);

			} catch (Exception e) {
			    throw new RuntimeException(e);
			}

		} else {

			order.setStatus(OrderStatus.CANCELLED);
			System.out.println("Order CANCELLED due to inventory failure");
			orderRepository.save(order);
		}
	}

	@KafkaListener(topics = "payment-response-topic", groupId = "order-group")
	public void finalizeOrder(PaymentResponse response) {

		System.out.println("Payment response received for order: " + response.getOrderId());

		Order order = orderRepository.findById(response.getOrderId()).orElse(null);

		if (order == null) return;

		if (response.isSuccess()) {

			order.setStatus(OrderStatus.COMPLETED);

		} else {

			order.setStatus(OrderStatus.CANCELLED);
			
			try {
			    InventoryRestoreEvent restoreEvent =
			            new InventoryRestoreEvent(order.getProductId(), order.getQuantity());

			    String payload = objectMapper.writeValueAsString(restoreEvent);

			    OutboxEvent outbox = new OutboxEvent();
			    outbox.setAggregateId(order.getId());
			    outbox.setAggregateType("ORDER");
			    outbox.setEventType("INVENTORY_RESTORE");
			    outbox.setPayload(payload);
			    outbox.setStatus("PENDING");

			    System.out.println("INVENTORY_RESTORE event saved to OUTBOX as PENDING");
			    
			    outboxRepository.save(outbox);

			} catch (Exception e) {
			    throw new RuntimeException(e);
			}
			
		}

		orderRepository.save(order);

		System.out.println("Order finalized: " + order.getStatus());
	}



}

