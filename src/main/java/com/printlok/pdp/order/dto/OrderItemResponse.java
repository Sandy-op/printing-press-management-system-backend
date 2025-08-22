package com.printlok.pdp.order.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {
	private String productName;
	private Integer quantity;
	private String size;
	private String customNote;
}