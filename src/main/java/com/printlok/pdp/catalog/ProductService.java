package com.printlok.pdp.catalog;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.printlok.pdp.catalog.dto.CategoryResponse;
import com.printlok.pdp.catalog.dto.ProductResponse;
import com.printlok.pdp.catalog.models.Category;
import com.printlok.pdp.catalog.models.Product;
import com.printlok.pdp.catalog.repositories.CategoryRepository;
import com.printlok.pdp.catalog.repositories.ProductRepository;
import com.printlok.pdp.common.dto.ResponseStructure;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	public ResponseEntity<ResponseStructure<List<ProductResponse>>> getAllProducts(Long categoryId) {
		List<Product> products;

		if (categoryId != null) {
			Optional<Category> category = categoryRepository.findById(categoryId);
			if (category.isEmpty()) {
				return buildResponse("Category not found", null, HttpStatus.NOT_FOUND);
			}
			products = productRepository.findByCategoryAndAvailableTrue(category.get());
		} else {
			products = productRepository.findByAvailableTrue();
		}

		List<ProductResponse> productResponses = products.stream().map(this::mapToResponse)
				.collect(Collectors.toList());
		return buildResponse("Products fetched successfully", productResponses, HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<ProductResponse>> getProductById(Long id) {
		Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
		return buildResponse("Product fetched successfully", mapToResponse(product), HttpStatus.OK);
	}

	public ResponseEntity<ResponseStructure<List<CategoryResponse>>> getAllCategories() {
		List<Category> categories = categoryRepository.findAll();
		List<CategoryResponse> categoryResponses = categories.stream().map(this::mapToResponse)
				.collect(Collectors.toList());
		return buildResponse("Categories fetched successfully", categoryResponses, HttpStatus.OK);
	}

	// --- Helper Methods ---

	private ProductResponse mapToResponse(Product product) {
		return ProductResponse.builder().id(product.getId()).name(product.getName())
				.description(product.getDescription()).price(product.getPrice()).imageUrl(product.getImageUrl())
				.categoryName(product.getCategory().getName()).available(product.getAvailable()).build();
	}

	private CategoryResponse mapToResponse(Category category) {
		return CategoryResponse.builder().id(category.getId()).name(category.getName())
				.description(category.getDescription()).build();
	}

	private <T> ResponseEntity<ResponseStructure<T>> buildResponse(String message, T data, HttpStatus status) {
		ResponseStructure<T> structure = new ResponseStructure<>();
		structure.setMessage(message);
		structure.setData(data);
		structure.setStatusCode(status.value());
		return ResponseEntity.status(status).body(structure);
	}
}
