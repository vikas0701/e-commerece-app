package com.ecommerce.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.inventoryservice.entity.ProcessedEvent;


public interface ProcessedEventRepository
        extends JpaRepository<ProcessedEvent, String> {

}
