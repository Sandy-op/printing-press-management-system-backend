package com.printlok.pdp.services.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.printlok.pdp.dto.ResponseStructure;
import com.printlok.pdp.dto.order.OrderItemRequest;
import com.printlok.pdp.dto.order.OrderItemResponse;
import com.printlok.pdp.dto.order.OrderRequest;
import com.printlok.pdp.dto.order.OrderResponse;

import com.printlok.pdp.models.catalog.Product;
import com.printlok.pdp.models.orders.Order;
import com.printlok.pdp.models.orders.OrderItem;
import com.printlok.pdp.models.user.User;
import com.printlok.pdp.repositories.catalog.ProductRepository;
import com.printlok.pdp.repositories.order.OrderRepository;
import com.printlok.pdp.repositories.user.UserRepository;
import com.printlok.pdp.services.user.UserDetailsImpl;
import com.printlok.pdp.utils.ERole;
import com.printlok.pdp.utils.OrderStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final ProductRepository productRepository;
	private final OrderRepository orderRepository;
	private final UserRepository userRepository;

	public ResponseEntity<ResponseStructure<OrderResponse>> placeOrder(OrderRequest request, UserDetailsImpl user) {
		User managedUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new RuntimeException("Authenticated user not found."));

		Order newOrder = Order.builder().createdAt(LocalDateTime.now()).status(OrderStatus.RECEIVED)
				.customer(managedUser).build();

		List<OrderItem> orderItems = request.getItems().stream()
				.map(itemRequest -> mapToOrderItem(itemRequest, newOrder)).collect(Collectors.toList());

		newOrder.setItems(orderItems);
		Order savedOrder = orderRepository.save(newOrder);

		ResponseStructure<OrderResponse> structure = new ResponseStructure<>();
		structure.setMessage("Order placed successfully");
		structure.setData(mapToOrderResponse(savedOrder));
		structure.setStatusCode(HttpStatus.CREATED.value());

		return ResponseEntity.status(HttpStatus.CREATED).body(structure);
	}

	public ResponseEntity<ResponseStructure<List<OrderResponse>>> getMyOrders(UserDetailsImpl user) {
		User managedUser = userRepository.findById(user.getId())
				.orElseThrow(() -> new RuntimeException("Authenticated user not found"));

		List<Order> orders = orderRepository.findByCustomer(managedUser);
		List<OrderResponse> responses = orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());

		ResponseStructure<List<OrderResponse>> structure = new ResponseStructure<>();
		structure.setMessage("Orders fetched successfully");
		structure.setData(responses);
		structure.setStatusCode(HttpStatus.OK.value());

		return ResponseEntity.ok(structure);
	}

	public ResponseEntity<ResponseStructure<OrderResponse>> getOrderById(Long id, UserDetailsImpl user) {
		Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

		if (!order.getCustomer().getId().equals(user.getId())) {
			ResponseStructure<OrderResponse> structure = new ResponseStructure<>();
			structure.setMessage("Access denied");
			structure.setData(null);
			structure.setStatusCode(HttpStatus.FORBIDDEN.value());
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(structure);
		}

		ResponseStructure<OrderResponse> structure = new ResponseStructure<>();
		structure.setMessage("Order details fetched successfully");
		structure.setData(mapToOrderResponse(order));
		structure.setStatusCode(HttpStatus.OK.value());

		return ResponseEntity.ok(structure);
	}

	@PreAuthorize("hasRole('ADMIN') or hasRole('OPERATOR')")
	public ResponseEntity<ResponseStructure<List<OrderResponse>>> getAllOrders(UserDetailsImpl userDetails) {
		User currentUser = userRepository.findById(userDetails.getId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

		List<Order> orders = isAdmin ? orderRepository.findAll() : orderRepository.findByAssignedOperator(currentUser);

		List<OrderResponse> responses = orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());

		ResponseStructure<List<OrderResponse>> structure = new ResponseStructure<>();
		structure.setMessage("Orders fetched successfully");
		structure.setData(responses);
		structure.setStatusCode(HttpStatus.OK.value());

		return ResponseEntity.ok(structure);
	}

	public ResponseEntity<ResponseStructure<OrderResponse>> approveOrderWithAssignment(Long orderId, Long operatorId,
			LocalDate deadline, UserDetailsImpl currentUser) {

		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

		if (order.getStatus() != OrderStatus.RECEIVED) {
			throw new RuntimeException("Only RECEIVED orders can be approved.");
		}

		if (deadline == null || deadline.isBefore(LocalDate.now())) {
			throw new RuntimeException("Valid deadline must be provided.");
		}

		User admin = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new RuntimeException("Admin user not found"));

		User operator = userRepository.findById(operatorId)
				.orElseThrow(() -> new RuntimeException("Operator user not found"));

		boolean isOperator = operator.getRoles().stream().anyMatch(role -> role.getRole() == ERole.OPERATOR);

		if (!isOperator) {
			throw new RuntimeException("Assigned user must have OPERATOR role.");
		}

		order.setDeadline(deadline);
		order.setStatus(OrderStatus.APPROVED);
		order.setAssignedOperator(operator);
		order.setReviewedBy(admin);
		order.setUpdatedAt(LocalDateTime.now());

		orderRepository.save(order);

		OrderResponse response = mapToOrderResponse(order);
		ResponseStructure<OrderResponse> structure = new ResponseStructure<>();
		structure.setMessage("Order approved and assigned to operator");
		structure.setData(response);
		structure.setStatusCode(HttpStatus.OK.value());

		return ResponseEntity.ok(structure);
	}

	public ResponseEntity<ResponseStructure<OrderResponse>> rejectOrder(Long orderId, String reason,
			UserDetailsImpl currentUser) {
		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

		if (order.getStatus() != OrderStatus.RECEIVED) {
			throw new RuntimeException("Only RECEIVED orders can be rejected.");
		}

		if (reason == null || reason.isBlank()) {
			throw new RuntimeException("Rejection reason must be provided.");
		}

		User admin = userRepository.findById(currentUser.getId())
				.orElseThrow(() -> new RuntimeException("Admin user not found"));

		order.setStatus(OrderStatus.REJECTED);
		order.setReviewedBy(admin);
		order.setUpdatedAt(LocalDateTime.now());
		order.setRejectionReason(reason);

		orderRepository.save(order);

		OrderResponse response = mapToOrderResponse(order);
		ResponseStructure<OrderResponse> structure = new ResponseStructure<>();
		structure.setMessage("Order rejected");
		structure.setData(response);
		structure.setStatusCode(HttpStatus.OK.value());

		return ResponseEntity.ok(structure);
	}

	public ResponseEntity<ResponseStructure<List<OrderResponse>>> getOrders(String status) {
		List<Order> orders;

		if (status == null || status.isBlank()) {
			orders = orderRepository.findAll();
		} else {
			try {
				OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
				orders = orderRepository.findByStatus(orderStatus);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException("Invalid order status: " + status);
			}
		}

		List<OrderResponse> responseList = orders.stream().map(this::mapToOrderResponse).toList();

		ResponseStructure<List<OrderResponse>> structure = new ResponseStructure<>();
		structure.setMessage("Fetched " + (status == null ? "all" : status.toUpperCase()) + " orders");
		structure.setData(responseList);
		structure.setStatusCode(HttpStatus.OK.value());

		return ResponseEntity.ok(structure);
	}

	public ResponseEntity<ResponseStructure<List<OrderResponse>>> getOrdersAssignedToOperator(UserDetailsImpl userDetails) {
		List<Order> orders = orderRepository.findByAssignedOperatorId(userDetails.getId());

		List<OrderResponse> responses = orders.stream().map(this::mapToOrderResponse).collect(Collectors.toList());

		ResponseStructure<List<OrderResponse>> structure = new ResponseStructure<>();
		structure.setMessage("Assigned orders fetched");
		structure.setData(responses);
		structure.setStatusCode(HttpStatus.OK.value());

		return ResponseEntity.ok(structure);
	}

	public ResponseEntity<ResponseStructure<OrderResponse>> updateOrderStatusByOperator(Long orderId, String status,
			UserDetailsImpl userDetails) {

		Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

		if (!order.getAssignedOperator().getId().equals(userDetails.getId())) {
			throw new AccessDeniedException("You are not assigned to this order");
		}

		OrderStatus newStatus;
		try {
			newStatus = OrderStatus.valueOf(status.toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid status: " + status);
		}

		order.setStatus(newStatus);
		order.setUpdatedAt(LocalDateTime.now());

		Order updatedOrder = orderRepository.save(order);

		ResponseStructure<OrderResponse> structure = new ResponseStructure<>();
		structure.setMessage("Order status updated");
		structure.setData(mapToOrderResponse(updatedOrder));
		structure.setStatusCode(HttpStatus.OK.value());

		return ResponseEntity.ok(structure);
	}

	// ------------------ Helper Methods ------------------

	private OrderItem mapToOrderItem(OrderItemRequest itemRequest, Order order) {
		Product product = productRepository.findById(itemRequest.getProductId())
				.orElseThrow(() -> new RuntimeException("Product not found with ID: " + itemRequest.getProductId()));

		return OrderItem.builder().product(product).quantity(itemRequest.getQuantity()).size(itemRequest.getSize())
				.customNote(itemRequest.getCustomNote()).order(order).build();
	}

	private OrderResponse mapToOrderResponse(Order order) {
		return OrderResponse.builder().id(order.getId()).status(order.getStatus().toString())
				.createdAt(order.getCreatedAt()).updatedAt(order.getUpdatedAt()).deadline(order.getDeadline())
				.rejectionReason(order.getRejectionReason())
				.customerName(order.getCustomer() != null ? order.getCustomer().getName() : null)
				.assignedOperatorName(
						order.getAssignedOperator() != null ? order.getAssignedOperator().getName() : null)
				.approvedByName(order.getReviewedBy() != null ? order.getReviewedBy().getName() : null)
				.items(order.getItems() != null ? order.getItems().stream().map(this::mapToOrderItemResponse).toList()
						: null)
				.build();
	}

	private OrderItemResponse mapToOrderItemResponse(OrderItem item) {
		return OrderItemResponse.builder().productName(item.getProduct().getName()).quantity(item.getQuantity())
				.size(item.getSize()).customNote(item.getCustomNote()).build();
	}

}
