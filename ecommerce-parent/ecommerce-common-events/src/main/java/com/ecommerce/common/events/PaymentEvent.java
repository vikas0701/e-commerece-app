package com.ecommerce.common.events;

import java.util.UUID;

public class PaymentEvent {

  private UUID eventId;
  private Long orderId;
  private Long userId;
  private Double amount;

  public PaymentEvent() {}

  public PaymentEvent(UUID eventId, Long orderId, Long userId, Double amount) {
      this.eventId = eventId;
      this.orderId = orderId;
      this.userId = userId;
      this.amount = amount;
  }

  public UUID getEventId() {
      return eventId;
  }

  public void setEventId(UUID eventId) {
      this.eventId = eventId;
  }

  public Long getOrderId() { return orderId; }
  public Long getUserId() { return userId; }
  public Double getAmount() { return amount; }

  public void setOrderId(Long orderId) { this.orderId = orderId; }
  public void setUserId(Long userId) { this.userId = userId; }
  public void setAmount(Double amount) { this.amount = amount; }
}
