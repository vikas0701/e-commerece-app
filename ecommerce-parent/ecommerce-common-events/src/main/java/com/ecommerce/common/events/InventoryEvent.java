package com.ecommerce.common.events;

import java.util.UUID;

public class InventoryEvent {

	private UUID eventId;
	private Long orderId;
	private String status;

	public InventoryEvent() {}

	public InventoryEvent(UUID eventId, Long orderId, String status) {
		this.eventId = eventId;
		this.orderId = orderId;
		this.status = status;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public UUID getEventId() {
		return eventId;
	}

	public void setEventId(UUID eventId) {
		this.eventId = eventId;
	}


}

