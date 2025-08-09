package com.printlok.pdp.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.printlok.pdp.dto.ResponseStructure;
import com.printlok.pdp.dto.catlog.CategoryResponse;
import com.printlok.pdp.dto.catlog.ProductResponse;
import com.printlok.pdp.services.catalog.ProductService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin("*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ResponseStructure<List<ProductResponse>>> getAllProducts(
            @RequestParam(required = false) Long categoryId) {
        return productService.getAllProducts(categoryId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseStructure<ProductResponse>> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/categories")
    public ResponseEntity<ResponseStructure<List<CategoryResponse>>> getAllCategories() {
        return productService.getAllCategories();
    }
}
