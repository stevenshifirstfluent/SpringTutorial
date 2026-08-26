package sg.edu.nus.validation.tutorial;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository
extends JpaRepository<Product, String> {


boolean existsByNameIgnoreCase(
    String name
);


boolean existsByNameIgnoreCaseAndIdNot(
    String name,
    String id
);
}
