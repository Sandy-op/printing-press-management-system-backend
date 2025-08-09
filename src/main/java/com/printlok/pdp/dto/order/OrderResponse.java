package com.printlok.pdp.dto.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDate deadline;

    private String rejectionReason;

    private String customerName;
    private String assignedOperatorName;
    private String approvedByName;

    private List<OrderItemResponse> items;
}