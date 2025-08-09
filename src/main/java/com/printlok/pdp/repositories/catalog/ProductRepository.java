package com.printlok.pdp.repositories.catalog;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.models.catalog.Category;
import com.printlok.pdp.models.catalog.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByAvailableTrue();

	List<Product> findByCategoryAndAvailableTrue(Category category);
}
