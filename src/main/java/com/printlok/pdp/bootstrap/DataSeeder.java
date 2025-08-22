//package com.printlok.pdp.bootstrap;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.printlok.pdp.catalog.models.Product;
//import com.printlok.pdp.catalog.repositories.CategoryRepository;
//import com.printlok.pdp.catalog.repositories.ProductRepository;
//import com.printlok.pdp.common.enums.OrderStatus;
//import com.printlok.pdp.order.models.Order;
//import com.printlok.pdp.order.models.OrderItem;
//import com.printlok.pdp.order.repositories.OrderRepository;
//import com.printlok.pdp.user.models.User;
//import com.printlok.pdp.user.repositories.UserRepository;
//
//@Configuration
//public class DataSeeder {
//
//	@Bean
//	CommandLineRunner run(CategoryRepository categoryRepo, ProductRepository productRepo, UserRepository userRepo,
//			OrderRepository orderRepo) {
//		return args -> {
//
//			// --- Seed categories and products as you already do ---
//			// e.g., productRepo.save(new Product(...));
//
//			// --- Fetch an existing customer (or create one dummy user) ---
//			User customer = userRepo.findById(1L) // assumes user with ID 1 exists
//					.orElseThrow(() -> new RuntimeException("Dummy customer not found"));
//
//			// --- Pick some products from DB ---
//			List<Product> products = productRepo.findAll();
//			if (products.isEmpty()) {
//				System.out.println("⚠ No products found, skipping order seeding.");
//				return;
//			}
//
//			// --- Create dummy orders ---
//			for (int i = 1; i <= 5; i++) {
//				Order order = Order.builder().createdAt(LocalDateTime.now().minusDays(i))
//						.updatedAt(LocalDateTime.now().minusDays(i)).deadline(LocalDate.now().plusDays(7))
//						.status(OrderStatus.RECEIVED).customer(customer).build();
//
//				// Pick 2 random products for order items
//				List<OrderItem> items = products.stream().limit(2)
//						.map(product -> OrderItem.builder().product(product).quantity((int) (Math.random() * 5) + 1)
//								.size("M").customNote("Sample note for " + product.getName()).order(order).build())
//						.toList();
//
//				order.setItems(items);
//
//				orderRepo.save(order);
//			}
//
//			System.out.println("✅ Dummy orders seeded successfully!");
//		};
//	}
//
////    @Bean
////    CommandLineRunner run(CategoryRepository categoryRepo, ProductRepository productRepo) {
////        return args -> {
////
////            // Category 1 - Books (8 products)
////            Category books = categoryRepo
////                    .save(Category.builder().name("Books").description("Educational and fictional books").build());
////
////            productRepo.save(Product.builder().name("Science Book").description("Class 10 CBSE science").price(180.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(books).build());
////            productRepo.save(Product.builder().name("English Workbook").description("Grammar and writing skills")
////                    .price(90.0).imageUrl("https://via.placeholder.com/150").available(true).category(books).build());
////            productRepo.save(Product.builder().name("Novel - The Great Story").description("A fictional adventure novel")
////                    .price(250.0).imageUrl("https://via.placeholder.com/150").available(true).category(books).build());
////            productRepo.save(Product.builder().name("Mathematics Reference Book").description("Advanced algebra concepts")
////                    .price(350.0).imageUrl("https://via.placeholder.com/150").available(true).category(books).build());
////            productRepo.save(Product.builder().name("History of Civilizations").description("World history reference")
////                    .price(400.0).imageUrl("https://via.placeholder.com/150").available(true).category(books).build());
////            productRepo.save(Product.builder().name("Children's Fairy Tales").description("Colorful illustrated storybook")
////                    .price(200.0).imageUrl("https://via.placeholder.com/150").available(true).category(books).build());
////            productRepo.save(Product.builder().name("Programming in Java").description("Beginner to advanced Java concepts")
////                    .price(500.0).imageUrl("https://via.placeholder.com/150").available(true).category(books).build());
////            productRepo.save(Product.builder().name("Cooking Recipes").description("Quick and easy meals cookbook")
////                    .price(320.0).imageUrl("https://via.placeholder.com/150").available(true).category(books).build());
////
////            // Category 2 - Electronics (8 products)
////            Category electronics = categoryRepo
////                    .save(Category.builder().name("Electronics").description("Gadgets and devices").build());
////
////            productRepo.save(Product.builder().name("Bluetooth Speaker").description("Portable wireless speaker").price(1200.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(electronics).build());
////            productRepo.save(Product.builder().name("Wireless Mouse").description("Ergonomic mouse with USB receiver")
////                    .price(450.0).imageUrl("https://via.placeholder.com/150").available(true).category(electronics).build());
////            productRepo.save(Product.builder().name("Smartphone Stand").description("Adjustable phone holder for desk")
////                    .price(300.0).imageUrl("https://via.placeholder.com/150").available(true).category(electronics).build());
////            productRepo.save(Product.builder().name("USB-C Cable").description("Fast charging and data transfer cable")
////                    .price(150.0).imageUrl("https://via.placeholder.com/150").available(true).category(electronics).build());
////            productRepo.save(Product.builder().name("Power Bank").description("10000mAh portable charger")
////                    .price(800.0).imageUrl("https://via.placeholder.com/150").available(true).category(electronics).build());
////            productRepo.save(Product.builder().name("Wireless Earbuds").description("Noise-cancelling earbuds")
////                    .price(2200.0).imageUrl("https://via.placeholder.com/150").available(true).category(electronics).build());
////            productRepo.save(Product.builder().name("Smartwatch").description("Fitness tracking smartwatch")
////                    .price(3500.0).imageUrl("https://via.placeholder.com/150").available(true).category(electronics).build());
////            productRepo.save(Product.builder().name("HD Webcam").description("1080p video calling webcam")
////                    .price(1500.0).imageUrl("https://via.placeholder.com/150").available(true).category(electronics).build());
////
////            // Category 3 - Stationery (7 products)
////            Category stationery = categoryRepo
////                    .save(Category.builder().name("Stationery").description("Office and school supplies").build());
////
////            productRepo.save(Product.builder().name("Gel Pen Set").description("Pack of 10 smooth-writing pens").price(120.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(stationery).build());
////            productRepo.save(Product.builder().name("A4 Notebook").description("200-page ruled notebook").price(80.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(stationery).build());
////            productRepo.save(Product.builder().name("Sticky Notes").description("Pack of 5 colors, 100 sheets each").price(60.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(stationery).build());
////            productRepo.save(Product.builder().name("Highlighter Set").description("Pack of 6 fluorescent colors").price(150.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(stationery).build());
////            productRepo.save(Product.builder().name("Drawing Sketchbook").description("A3 size with thick sheets").price(200.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(stationery).build());
////            productRepo.save(Product.builder().name("Office File Folder").description("Set of 3 with labels").price(250.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(stationery).build());
////            productRepo.save(Product.builder().name("Whiteboard Markers").description("Pack of 4 colors").price(120.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(stationery).build());
////
////            // Category 4 - Clothing (7 products)
////            Category clothing = categoryRepo
////                    .save(Category.builder().name("Clothing").description("Men's and women's apparel").build());
////
////            productRepo.save(Product.builder().name("Cotton T-Shirt").description("Comfortable casual wear").price(350.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(clothing).build());
////            productRepo.save(Product.builder().name("Denim Jeans").description("Slim fit blue jeans").price(1200.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(clothing).build());
////            productRepo.save(Product.builder().name("Winter Jacket").description("Warm and stylish jacket").price(2500.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(clothing).build());
////            productRepo.save(Product.builder().name("Formal Shirt").description("Office wear, cotton fabric").price(800.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(clothing).build());
////            productRepo.save(Product.builder().name("Sports Shorts").description("Lightweight running shorts").price(400.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(clothing).build());
////            productRepo.save(Product.builder().name("Hoodie").description("Casual fleece hoodie").price(900.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(clothing).build());
////            productRepo.save(Product.builder().name("Summer Dress").description("Floral cotton dress").price(1300.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(clothing).build());
////
////            // Category 5 - Sports (6 products)
////            Category sports = categoryRepo
////                    .save(Category.builder().name("Sports").description("Sports gear and equipment").build());
////
////            productRepo.save(Product.builder().name("Football").description("Standard size football").price(600.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(sports).build());
////            productRepo.save(Product.builder().name("Badminton Racket").description("Lightweight racket for beginners")
////                    .price(750.0).imageUrl("https://via.placeholder.com/150").available(true).category(sports).build());
////            productRepo.save(Product.builder().name("Yoga Mat").description("Non-slip surface yoga mat").price(500.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(sports).build());
////            productRepo.save(Product.builder().name("Cricket Bat").description("Lightweight wooden bat").price(1500.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(sports).build());
////            productRepo.save(Product.builder().name("Skipping Rope").description("Adjustable length for workouts").price(200.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(sports).build());
////            productRepo.save(Product.builder().name("Basketball").description("Official size basketball").price(900.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(sports).build());
////
////            // Category 6 - Home & Kitchen (7 products)
////            Category homeKitchen = categoryRepo
////                    .save(Category.builder().name("Home & Kitchen").description("Essentials for your home").build());
////
////            productRepo.save(Product.builder().name("Non-stick Frying Pan").description("Durable and easy to clean").price(700.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(homeKitchen).build());
////            productRepo.save(Product.builder().name("Electric Kettle").description("1.5L capacity, quick boil").price(1200.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(homeKitchen).build());
////            productRepo.save(Product.builder().name("Dinner Set").description("16-piece ceramic dinnerware").price(2500.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(homeKitchen).build());
////            productRepo.save(Product.builder().name("Vacuum Cleaner").description("Compact and powerful suction").price(4000.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(homeKitchen).build());
////            productRepo.save(Product.builder().name("Blender").description("Multi-speed kitchen blender").price(1500.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(homeKitchen).build());
////            productRepo.save(Product.builder().name("Table Lamp").description("LED desk lamp with adjustable brightness").price(800.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(homeKitchen).build());
////            productRepo.save(Product.builder().name("Wall Clock").description("Minimalist design clock").price(600.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(homeKitchen).build());
////
////            // Category 7 - Toys (7 products)
////            Category toys = categoryRepo
////                    .save(Category.builder().name("Toys").description("Fun and learning for kids").build());
////
////            productRepo.save(Product.builder().name("Building Blocks Set").description("100-piece creative blocks").price(800.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(toys).build());
////            productRepo.save(Product.builder().name("Remote Control Car").description("High-speed RC car").price(1500.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(toys).build());
////            productRepo.save(Product.builder().name("Stuffed Teddy Bear").description("Soft plush toy").price(500.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(toys).build());
////            productRepo.save(Product.builder().name("Puzzle Game").description("500-piece jigsaw puzzle").price(400.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(toys).build());
////            productRepo.save(Product.builder().name("Action Figure Set").description("Superhero characters collection").price(900.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(toys).build());
////            productRepo.save(Product.builder().name("Lego Starter Kit").description("Creative building toy set").price(2000.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(toys).build());
////            productRepo.save(Product.builder().name("Musical Keyboard").description("Mini piano for kids").price(1200.0)
////                    .imageUrl("https://via.placeholder.com/150").available(true).category(toys).build());
////        };
////    }
//}
