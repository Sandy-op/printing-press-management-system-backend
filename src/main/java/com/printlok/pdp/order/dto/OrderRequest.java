package com.printlok.pdp.order.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
	private List<OrderItemRequest> items;
}
