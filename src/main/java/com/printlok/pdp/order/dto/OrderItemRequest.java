package com.printlok.pdp.order.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long productId;
    private Integer quantity;
    private String size;
    private String customNote;
}