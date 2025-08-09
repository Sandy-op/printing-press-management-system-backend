package com.printlok.pdp.dto.order;

import java.util.List;

import lombok.Data;

@Data
public class OrderRequest {
	private List<OrderItemRequest> items;
}
