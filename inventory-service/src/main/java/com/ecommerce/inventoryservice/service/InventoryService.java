package com.ecommerce.inventoryservice.service;

import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ecommerce.common.events.InventoryEvent;
import com.ecommerce.common.events.InventoryRestoreEvent;
import com.ecommerce.common.events.OrderEvent;
import com.ecommerce.common.exception.BusinessException;
import com.ecommerce.common.exception.ErrorCode;
//import com.ecommerce.inventoryservice.dto.OrderEvent;
//import com.ecommerce.inventoryservice.dto.InventoryEvent;
import com.ecommerce.inventoryservice.entity.Inventory;
import com.ecommerce.inventoryservice.repository.InventoryRepository;

@Service
public class InventoryService {

	private final InventoryRepository inventoryRepository;
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public InventoryService(InventoryRepository inventoryRepository,
			KafkaTemplate<String, Object> kafkaTemplate) {
		this.inventoryRepository = inventoryRepository;
		this.kafkaTemplate = kafkaTemplate;
	}

	@RetryableTopic(
			attempts = "3",
			backoff = @Backoff(delay = 2000),
			dltTopicSuffix = "-dlt"
			)
	@KafkaListener(topics = "order-topic", groupId = "inventory-group-v4")
	public void handleOrder(OrderEvent event) {

		System.out.println("order-topic event received: " + event);
		
		Inventory inventory = inventoryRepository.findById(event.getProductId())
				.orElse(null);
		System.out.println("inventory " + inventory);
		if (inventory == null) {
//			throw new RuntimeException("Inventory not found for product " + event.getProductId());
			if (inventory == null) {
			    throw new BusinessException(
			            ErrorCode.INVENTORY_NOT_AVAILABLE,
			            "Inventory not found for product " + event.getProductId()
			    );
			}
		}

		// 🟢 Stock available
		if (inventory.getStock() >= event.getQuantity()) {

			inventory.setStock(inventory.getStock() - event.getQuantity());
			inventoryRepository.save(inventory);

			System.out.println("Inventory published INVENTORY_CONFIRMED for order: " + event.getOrderId());

			kafkaTemplate.send("inventory-topic",
					new InventoryEvent(event.getOrderId(), "INVENTORY_CONFIRMED"));

		} else {

			// 🟡 Business failure (NOT system failure)
			kafkaTemplate.send("inventory-topic",
					new InventoryEvent(event.getOrderId(), "CANCELLED"));
		}
	}


//	// 🔥 Dead Letter Topic Listener
//	@KafkaListener(topics = "inventory-topic-dlt", groupId = "inventory-dlt-v4-group")
//	public void handleDLT(Object failedMessage) {
//		System.out.println("⚠ Message moved to DLT: " + failedMessage);
//	}

	@DltHandler
	public void handleDlt(OrderEvent event) {
		System.out.println("⚠ Message permanently failed and sent to DLT: " + event);
	}
	
	@KafkaListener(
		    topics = "inventory-restore-topic",
		    groupId = "inventory-group-v4"
		)
		public void restoreInventory(InventoryRestoreEvent event) {

		    System.out.println("Restoring inventory for product: " + event.getProductId());

		    Inventory inventory = inventoryRepository
		            .findById(event.getProductId())
		            .orElseThrow();

		    inventory.setStock(inventory.getStock() + event.getQuantity());

		    inventoryRepository.save(inventory);

		    System.out.println("Inventory restored successfully");
		}

}