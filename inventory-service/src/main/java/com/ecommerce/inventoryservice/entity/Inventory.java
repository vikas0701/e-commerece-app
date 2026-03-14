package com.ecommerce.inventoryservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    private Long productId;

    private Integer stock;

    public Inventory() {}

    public Inventory(Long productId, Integer stock) {
        this.productId = productId;
        this.stock = stock;
    }

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	@Override
	public String toString() {
		return "Inventory [productId=" + productId + ", stock=" + stock + "]";
	}

}
