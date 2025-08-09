package com.printlok.pdp.dto.order;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long productId;
    private Integer quantity;
    private String size;
    private String customNote;
}