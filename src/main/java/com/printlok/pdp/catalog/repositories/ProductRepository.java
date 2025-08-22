package com.printlok.pdp.catalog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.catalog.models.Category;
import com.printlok.pdp.catalog.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByAvailableTrue();

	List<Product> findByCategoryAndAvailableTrue(Category category);
}
