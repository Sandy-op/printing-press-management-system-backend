package com.printlok.pdp.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.printlok.pdp.catalog.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
