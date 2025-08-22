package com.printlok.pdp.order.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ApproveOrderRequest {
    private Long operatorId;
    private LocalDate deadline;
}
