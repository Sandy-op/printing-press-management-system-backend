package com.printlok.pdp.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OperatorResponse {
	private Long id;
	private String name;
	private String email;
}