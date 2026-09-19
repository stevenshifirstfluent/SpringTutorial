package sg.edu.nus.validation.tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.validation.tutorial.model.Product;

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
