package com.printlok.pdp.operator;

import java.util.List;

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
import com.printlok.pdp.order.OrderService;
import com.printlok.pdp.order.dto.OrderResponse;
import com.printlok.pdp.user.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/operator/orders")
@PreAuthorize("hasRole('OPERATOR')")
@RequiredArgsConstructor
public class OperatorOrderController {

	private final OrderService orderService;

	@GetMapping
	public ResponseEntity<ResponseStructure<List<OrderResponse>>> getAssignedOrders(
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return orderService.getOrdersAssignedToOperator(userDetails);
	}

	@PutMapping("/{orderId}/update-status")
	public ResponseEntity<ResponseStructure<OrderResponse>> updateOrderStatus(@PathVariable Long orderId,
			@RequestParam String newStatus, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		return orderService.updateOrderStatusByOperator(orderId, newStatus, userDetails);
	}
}
