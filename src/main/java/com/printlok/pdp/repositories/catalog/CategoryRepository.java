package com.printlok.pdp.repositories.catalog;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.models.catalog.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
