package com.printlok.pdp.repositories.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.models.orders.Order;
import com.printlok.pdp.models.user.User;
import com.printlok.pdp.utils.enums.OrderStatus;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByCustomer(User user);
	List<Order> findByAssignedOperator(User assignedOperator);
	List<Order> findByStatus(OrderStatus status);
	List<Order> findByAssignedOperatorId(Long operatorId);


}