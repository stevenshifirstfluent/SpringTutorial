package sg.edu.nus.restful.session1.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.restful.session1.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByActiveTrueOrderByIdAsc();

    Optional<Product> findByIdAndActiveTrue(Long id);

    List<Product> findByBrandIgnoreCaseAndActiveTrueOrderByIdAsc(String brand);

    List<Product> findByCategoryIdAndActiveTrueOrderByIdAsc(Long categoryId);

    List<Product> findByBrandIgnoreCaseAndCategoryIdAndActiveTrueOrderByIdAsc(
            String brand, Long categoryId);
}
