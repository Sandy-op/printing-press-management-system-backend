package com.printlok.pdp.role.dto;

import java.time.LocalDateTime;

import com.printlok.pdp.common.enums.RequestStatus;

import lombok.Data;

@Data
public class RoleUpgradeResponse {
	private Long requestId;
	private String companyName;
	private String gstNumber;
	private String userEmail;
	private String customNote;
	private RequestStatus status;
	private LocalDateTime requestDate;
}
