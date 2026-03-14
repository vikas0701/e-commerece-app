package com.ecommerce.orderservice.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;
	private Long productId;
	private Integer quantity;
	private Double totalPrice;
	//    private String status;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;


	public Order() {}

	    public Order(Long userId, Long productId, Integer quantity, Double totalPrice, OrderStatus status) {
	        this.userId = userId;
	        this.productId = productId;
	        this.quantity = quantity;
	        this.totalPrice = totalPrice;
	        this.status = status;
	    }

	public Order(Long id, Long userId, Long productId, Integer quantity, Double totalPrice, OrderStatus status) {
		super();
		this.id = id;
		this.userId = userId;
		this.productId = productId;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.status = status;
	}

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", userId=" + userId + ", productId=" + productId + ", quantity=" + quantity
				+ ", totalPrice=" + totalPrice + ", status=" + status + "]";
	}

//	public String getStatus() {
//		return status;
//	}
//
//	public void setStatus(String status) {
//		this.status = status;
//	}


}
