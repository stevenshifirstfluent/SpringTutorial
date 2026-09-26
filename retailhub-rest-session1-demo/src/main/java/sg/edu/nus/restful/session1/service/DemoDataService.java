package sg.edu.nus.restful.session1.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.nus.restful.session1.dto.ProductResponse;
import sg.edu.nus.restful.session1.model.Product;
import sg.edu.nus.restful.session1.repository.ProductRepository;

/**
 * Teaching helper only. It resets the H2 demo database so classroom scenarios
 * always start with predictable product IDs. It is not part of the REST design.
 */
@Service
public class DemoDataService {

    private final JdbcTemplate jdbcTemplate;
    private final ProductRepository productRepository;
    private final ProductService productService;

    public DemoDataService(JdbcTemplate jdbcTemplate,
                           ProductRepository productRepository,
                           ProductService productService) {
        this.jdbcTemplate = jdbcTemplate;
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @Transactional
    public List<ProductResponse> reset() {
        productRepository.deleteAllInBatch();
        productRepository.flush();
        jdbcTemplate.execute("ALTER TABLE products ALTER COLUMN id RESTART WITH 1");

        productRepository.saveAll(List.of(
                product("Chef Knife", "99.90", 12, 1L, "Wusthof"),
                product("Cast Iron Pan", "79.00", 8, 2L, "Lodge"),
                product("Coffee Grinder", "149.50", 5, 3L, "Baratza"),
                product("Chef Knife Classic", "129.90", 7, 1L, "Wusthof"),
                product("Digital Scale", "39.90", 20, 3L, "Hario")
        ));
        productRepository.flush();

        return productService.findAllActive(null, null);
    }

    private Product product(String name, String price, int stock, Long categoryId, String brand) {
        return new Product(name, new BigDecimal(price), stock, categoryId, brand, true);
    }
}
