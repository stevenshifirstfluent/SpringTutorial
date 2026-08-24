package sg.edu.nus.springcontroller.tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.edu.nus.springcontroller.tutorial.model.Product;

/*
 * Repository used to perform database
 * operations for Product entities.
 *
 * JpaRepository<Product, Integer>
 *
 * Product:
 *   the entity managed by this repository
 *
 * Integer:
 *   the Java type of Product's primary key
 */
public interface ProductRepository
        extends JpaRepository<Product, Integer> {

}
