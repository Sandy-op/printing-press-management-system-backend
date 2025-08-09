package com.printlok.pdp.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.printlok.pdp.models.catalog.Category;
import com.printlok.pdp.models.catalog.Product;
import com.printlok.pdp.repositories.catalog.CategoryRepository;
import com.printlok.pdp.repositories.catalog.ProductRepository;

@Configuration
public class DataSeeder {

	@Bean
	CommandLineRunner run(CategoryRepository categoryRepo, ProductRepository productRepo) {
		return args -> {

			// Category 1 - Books
			Category books = categoryRepo
					.save(Category.builder().name("Books").description("Educational and fictional books").build());

			productRepo.save(Product.builder().name("Science Book").description("Class 10 CBSE science").price(180.0)
					.imageUrl("https://via.placeholder.com/150").available(true).category(books).build());

			productRepo.save(Product.builder().name("English Workbook").description("Grammar and writing skills")
					.price(90.0).imageUrl("https://via.placeholder.com/150").available(true).category(books).build());

			productRepo.save(Product.builder().name("Novel - The Great Story")
					.description("A fictional adventure novel").price(250.0).imageUrl("https://via.placeholder.com/150")
					.available(true).category(books).build());

			// Category 2 - Electronics
			Category electronics = categoryRepo
					.save(Category.builder().name("Electronics").description("Gadgets and devices").build());

			productRepo.save(
					Product.builder().name("Bluetooth Speaker").description("Portable wireless speaker").price(1200.0)
							.imageUrl("https://via.placeholder.com/150").available(true).category(electronics).build());

			productRepo.save(Product.builder().name("Wireless Mouse").description("Ergonomic mouse with USB receiver")
					.price(450.0).imageUrl("https://via.placeholder.com/150").available(true).category(electronics)
					.build());

			productRepo.save(Product.builder().name("Smartphone Stand").description("Adjustable phone holder for desk")
					.price(300.0).imageUrl("https://via.placeholder.com/150").available(true).category(electronics)
					.build());

			// Category 3 - Stationery
			Category stationery = categoryRepo
					.save(Category.builder().name("Stationery").description("Office and school supplies").build());

			productRepo.save(
					Product.builder().name("Gel Pen Set").description("Pack of 10 smooth-writing pens").price(120.0)
							.imageUrl("https://via.placeholder.com/150").available(true).category(stationery).build());

			productRepo.save(Product.builder().name("A4 Notebook").description("200-page ruled notebook").price(80.0)
					.imageUrl("https://via.placeholder.com/150").available(true).category(stationery).build());

			productRepo.save(
					Product.builder().name("Sticky Notes").description("Pack of 5 colors, 100 sheets each").price(60.0)
							.imageUrl("https://via.placeholder.com/150").available(true).category(stationery).build());

			// Category 4 - Clothing
			Category clothing = categoryRepo
					.save(Category.builder().name("Clothing").description("Men's and women's apparel").build());

			productRepo
					.save(Product.builder().name("Cotton T-Shirt").description("Comfortable casual wear").price(350.0)
							.imageUrl("https://via.placeholder.com/150").available(true).category(clothing).build());

			productRepo.save(Product.builder().name("Denim Jeans").description("Slim fit blue jeans").price(1200.0)
					.imageUrl("https://via.placeholder.com/150").available(true).category(clothing).build());

			productRepo
					.save(Product.builder().name("Winter Jacket").description("Warm and stylish jacket").price(2500.0)
							.imageUrl("https://via.placeholder.com/150").available(true).category(clothing).build());

			// Category 5 - Sports
			Category sports = categoryRepo
					.save(Category.builder().name("Sports").description("Sports gear and equipment").build());

			productRepo.save(Product.builder().name("Football").description("Standard size football").price(600.0)
					.imageUrl("https://via.placeholder.com/150").available(true).category(sports).build());

			productRepo.save(Product.builder().name("Badminton Racket").description("Lightweight racket for beginners")
					.price(750.0).imageUrl("https://via.placeholder.com/150").available(true).category(sports).build());

			productRepo.save(Product.builder().name("Yoga Mat").description("Non-slip surface yoga mat").price(500.0)
					.imageUrl("https://via.placeholder.com/150").available(true).category(sports).build());

		};
	}
}
