package com.printlok.pdp.catalog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
	private Long id;
	private String name;
	private String description;
}