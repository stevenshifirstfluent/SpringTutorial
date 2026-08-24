package sg.edu.nus.springcontroller.tutorial;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import sg.edu.nus.springcontroller.tutorial.model.Product;
import sg.edu.nus.springcontroller.tutorial.repository.ProductRepository;

import java.time.LocalDate;

/*
 * Loads sample Product records when
 * the application starts.
 *
 * CommandLineRunner runs automatically
 * after the Spring application context
 * has been created.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {

        /*
         * Insert sample Product 1.
         */
        Product product1 = new Product(
                "Laptop",
                "Dell",
                "Business laptop",
                1599.00,
                LocalDate.of(2026, 8, 1)
        );

        productRepository.save(product1);


        /*
         * Insert sample Product 2.
         */
        Product product2 = new Product(
                "Monitor",
                "LG",
                "27-inch monitor",
                399.00,
                LocalDate.of(2026, 7, 15)
        );

        productRepository.save(product2);
    }
}
