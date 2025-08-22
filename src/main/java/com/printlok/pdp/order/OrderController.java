package com.printlok.pdp.order;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.printlok.pdp.common.dto.ResponseStructure;
import com.printlok.pdp.order.dto.OrderRequest;
import com.printlok.pdp.order.dto.OrderResponse;
import com.printlok.pdp.user.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PostMapping
	public ResponseEntity<ResponseStructure<OrderResponse>> placeOrder(@RequestBody OrderRequest request,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return orderService.placeOrder(request, userDetails);
	}

	@GetMapping
	public ResponseEntity<ResponseStructure<List<OrderResponse>>> getMyOrders(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return orderService.getMyOrders(userDetails);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<ResponseStructure<OrderResponse>> getOrderById(@PathVariable Long orderId,
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return orderService.getOrderById(orderId, userDetails);
	}
}