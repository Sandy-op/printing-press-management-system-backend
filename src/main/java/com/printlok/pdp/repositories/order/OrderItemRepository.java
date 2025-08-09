package com.printlok.pdp.repositories.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.models.orders.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}