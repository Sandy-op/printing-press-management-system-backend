package com.printlok.pdp.order.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.common.enums.OrderStatus;
import com.printlok.pdp.order.models.Order;
import com.printlok.pdp.user.models.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByCustomer(User user);
	List<Order> findByAssignedOperator(User assignedOperator);
	List<Order> findByStatus(OrderStatus status);
	List<Order> findByAssignedOperatorId(Long operatorId);


}