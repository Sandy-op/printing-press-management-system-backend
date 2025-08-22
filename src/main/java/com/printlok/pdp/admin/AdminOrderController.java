package com.printlok.pdp.admin;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.printlok.pdp.common.dto.ResponseStructure;
import com.printlok.pdp.operator.OperatorService;
import com.printlok.pdp.order.OrderService;
import com.printlok.pdp.order.dto.OrderResponse;
import com.printlok.pdp.user.UserDetailsImpl;
import com.printlok.pdp.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminOrderController {

	private final OrderService orderService;
	private final OperatorService operatorService;

	@GetMapping("/orders")
	public ResponseEntity<ResponseStructure<List<OrderResponse>>> getAllOrders(
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return orderService.getAllOrders(userDetails);
	}

	@GetMapping("/orders/filter-by-status")
	public ResponseEntity<ResponseStructure<List<OrderResponse>>> getOrdersByStatus(
			@RequestParam String status) {
		return orderService.getOrders(status);
	}

	@PutMapping("/orders/{orderId}/approve")
	public ResponseEntity<ResponseStructure<OrderResponse>> approveOrder(@PathVariable Long orderId,
			@RequestParam Long operatorId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return orderService.approveOrderWithAssignment(orderId, operatorId, deadline, userDetails);
	}

	@PutMapping("/orders/{orderId}/reject")
	public ResponseEntity<ResponseStructure<OrderResponse>> rejectOrder(@PathVariable Long orderId,
			@RequestParam String reason, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return orderService.rejectOrder(orderId, reason, userDetails);
	}

	@GetMapping("/operators")
	public ResponseEntity<ResponseStructure<List<UserResponse>>> getAllOperators() {
		return operatorService.getAllOperators();
	}
} 
