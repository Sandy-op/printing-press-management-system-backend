package com.printlok.pdp.catalog.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
	private Long id;
	private String name;
	private String description;
	private Double price;
	private String imageUrl;
	private String categoryName;
	private Boolean available;
}