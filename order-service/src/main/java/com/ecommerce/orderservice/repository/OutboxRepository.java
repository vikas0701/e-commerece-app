package com.ecommerce.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.orderservice.outbox.entity.OutboxEvent;

import java.util.List;

public interface OutboxRepository extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findTop50ByStatus(String status);
}
