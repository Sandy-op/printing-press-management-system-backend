package com.printlok.pdp.order.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.order.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}