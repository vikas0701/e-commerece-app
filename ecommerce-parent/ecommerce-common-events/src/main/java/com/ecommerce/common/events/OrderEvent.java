package com.ecommerce.common.events;

import java.util.UUID;

public class OrderEvent {

	private UUID eventId;
  private Long orderId;
  private Long userId;
  private Long productId;
  private Integer quantity;

  public OrderEvent() {}

	public OrderEvent(Long orderId, Long userId, Long productId, Integer quantity) {
	super();
	this.orderId = orderId;
	this.userId = userId;
	this.productId = productId;
	this.quantity = quantity;
}

	public OrderEvent(UUID eventId, Long orderId, Long userId, Long productId, Integer quantity) {
		super();
		this.eventId = eventId;
		this.orderId = orderId;
		this.userId = userId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "OrderEvent [orderId=" + orderId + ", userId=" + userId + ", productId=" + productId + ", quantity="
				+ quantity + "]";
	}

	public UUID getEventId() {
		return eventId;
	}

	public void setEventId(UUID eventId) {
		this.eventId = eventId;
	}

  
}