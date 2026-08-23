package sg.iss.nus.spring.tutorial.jpa.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
    CommandLineRunner loadDummyData(
            UserRepository userRepository,
            CartRepository cartRepository,
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            TagRepository tagRepository,
            PurchaseOrderRepository purchaseOrderRepository) {

        return args -> {

            // =====================================================
            // 1. CREATE CATEGORIES
            // =====================================================

            Category electronics = new Category();
            electronics.setName("Electronics");
            electronics.setDescription(
                    "Electronic devices and accessories"
            );

            Category books = new Category();
            books.setName("Books");
            books.setDescription(
                    "Books and learning materials"
            );

            categoryRepository.save(electronics);
            categoryRepository.save(books);


            // =====================================================
            // 2. CREATE TAGS
            // =====================================================

            Tag popular = new Tag();
            popular.setName("Popular");

            Tag sale = new Tag();
            sale.setName("Sale");

            Tag newArrival = new Tag();
            newArrival.setName("New Arrival");

            tagRepository.save(popular);
            tagRepository.save(sale);
            tagRepository.save(newArrival);


            // =====================================================
            // 3. CREATE PRODUCTS
            // =====================================================

            Product laptop = new Product();
            laptop.setName("Business Laptop");
            laptop.setDescription(
                    "14-inch laptop for office and development"
            );
            laptop.setBrand("TechPro");
            laptop.setImageUrl("/images/laptop.jpg");
            laptop.setPrice(1500.00);
            laptop.setDiscountPercent(10.0);
            laptop.setStockQuantity(20);
            laptop.setActive(true);

            // Many-to-One
            laptop.setCategory(electronics);

            // Many-to-Many
            laptop.addTag(popular);
            laptop.addTag(sale);


            Product headphones = new Product();
            headphones.setName("Wireless Headphones");
            headphones.setDescription(
                    "Bluetooth noise-cancelling headphones"
            );
            headphones.setBrand("SoundMax");
            headphones.setImageUrl("/images/headphones.jpg");
            headphones.setPrice(250.00);
            headphones.setDiscountPercent(5.0);
            headphones.setStockQuantity(50);
            headphones.setActive(true);

            headphones.setCategory(electronics);

            headphones.addTag(popular);
            headphones.addTag(newArrival);


            Product springBook = new Product();
            springBook.setName("Spring Boot Guide");
            springBook.setDescription(
                    "Introduction to Spring Boot and Spring Data JPA"
            );
            springBook.setBrand("Tech Publishing");
            springBook.setImageUrl("/images/spring-book.jpg");
            springBook.setPrice(60.00);
            springBook.setDiscountPercent(0.0);
            springBook.setStockQuantity(100);
            springBook.setActive(true);

            springBook.setCategory(books);
            springBook.addTag(popular);


            productRepository.save(laptop);
            productRepository.save(headphones);
            productRepository.save(springBook);


            // =====================================================
            // 4. CREATE USER
            // =====================================================

            User user = new User();

            user.setUsername("student1");
            user.setEmail("student1@example.com");
            user.setPassword("password123");

            user.setFirstName("John");
            user.setLastName("Tan");

            user.setBirthDate(
                    LocalDate.of(2000, 5, 15)
            );

            user.setCreatedAt(
                    LocalDateTime.now()
            );

            user.setActive(true);

            userRepository.save(user);


            // =====================================================
            // 5. CREATE CART
            // =====================================================

            Cart cart = new Cart();

            cart.setCreatedAt(
                    LocalDateTime.now()
            );

            // Cart is owning side of User-Cart relationship
            cart.setUser(user);


            // =====================================================
            // 6. CREATE CART ITEM #1
            // =====================================================

            CartItem cartItem1 = new CartItem();

            cartItem1.setProduct(laptop);
            cartItem1.setQuantity(1);
            cartItem1.setAddedAt(
                    LocalDateTime.now()
            );


            // =====================================================
            // 7. CREATE CART ITEM #2
            // =====================================================

            CartItem cartItem2 = new CartItem();

            cartItem2.setProduct(headphones);
            cartItem2.setQuantity(2);
            cartItem2.setAddedAt(
                    LocalDateTime.now()
            );


            // =====================================================
            // 8. ADD CART ITEMS TO CART
            // =====================================================

            /*
             * addItem() updates both sides:
             *
             * cart.getItems().add(item)
             * item.setCart(cart)
             */
            cart.addItem(cartItem1);
            cart.addItem(cartItem2);

            /*
             * Cart -> CartItem uses CascadeType.ALL,
             * so CartItems are saved automatically.
             */
            cartRepository.save(cart);

            // Keep Java object relationship synchronized
            user.setCart(cart);


            // =====================================================
            // 9. CREATE PURCHASE ORDER
            // =====================================================

            PurchaseOrder order = new PurchaseOrder();

            // Many PurchaseOrders -> one User
            order.setUser(user);

            order.setShippingAddress(
                    "123 Orchard Road, Singapore"
            );

            order.setNotes(
                    "Please deliver during office hours"
            );

            // Enum
            order.setStatus(
                    OrderStatus.CONFIRMED
            );

            order.setOrderDate(
                    LocalDateTime.now()
            );

            order.setDeliveredAt(null);


            // =====================================================
            // 10. CREATE ORDER ITEM #1
            // =====================================================

            OrderItem orderItem1 = new OrderItem();

            orderItem1.setProduct(laptop);
            orderItem1.setQuantity(1);

            /*
             * Historical price snapshot:
             *
             * Original price = $1500
             * Discount = 10%
             *
             * Unit price at checkout = $1350
             */
            orderItem1.setUnitPrice(1350.00);


            // =====================================================
            // 11. CREATE ORDER ITEM #2
            // =====================================================

            OrderItem orderItem2 = new OrderItem();

            orderItem2.setProduct(headphones);
            orderItem2.setQuantity(2);

            /*
             * Original price = $250
             * Discount = 5%
             *
             * Unit price at checkout = $237.50
             */
            orderItem2.setUnitPrice(237.50);


            // =====================================================
            // 12. ADD ORDER ITEMS
            // =====================================================

            /*
             * addItem() updates both sides:
             *
             * order.getItems().add(item)
             * item.setPurchaseOrder(order)
             */
            order.addItem(orderItem1);
            order.addItem(orderItem2);


            // =====================================================
            // 13. CALCULATE ORDER TOTAL
            // =====================================================

            double orderTotal =
                    orderItem1.getSubtotal()
                            + orderItem2.getSubtotal();

            order.setTotalAmount(orderTotal);


            // =====================================================
            // 14. SAVE PURCHASE ORDER
            // =====================================================

            /*
             * PurchaseOrder -> OrderItem uses CascadeType.ALL,
             * so OrderItems are saved automatically.
             */
            purchaseOrderRepository.save(order);


            // =====================================================
            // 15. DISPLAY TEST RESULTS
            // =====================================================

            System.out.println();
            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "SHOPPING CART DUMMY DATA LOADED"
            );

            System.out.println(
                    "=========================================="
            );

            System.out.println(
                    "User: "
                            + user.getFirstName()
                            + " "
                            + user.getLastName()
            );

            System.out.println(
                    "Username: "
                            + user.getUsername()
            );

            System.out.println();

            System.out.println(
                    "Cart total items: "
                            + cart.getTotalItems()
            );

            System.out.println(
                    "Cart total: $"
                            + cart.getTotal()
            );

            System.out.println();

            System.out.println(
                    "Order status: "
                            + order.getStatus()
            );

            System.out.println(
                    "Order total: $"
                            + order.getTotalAmount()
            );

            System.out.println();

            System.out.println(
                    "Laptop category: "
                            + laptop.getCategory().getName()
            );

            System.out.println(
                    "Laptop tags: "
                            + laptop.getTags()
                                    .stream()
                                    .map(Tag::getName)
                                    .toList()
            );

            System.out.println(
                    "=========================================="
            );
        };
	}

}
