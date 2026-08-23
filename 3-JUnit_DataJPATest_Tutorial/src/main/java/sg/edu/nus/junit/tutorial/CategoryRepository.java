package sg.edu.nus.junit.tutorial;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository
        extends JpaRepository<Category, Long> {

    /*
     * Spring Data JPA derives the query from the method name.
     *
     * Conceptually:
     *
     * SELECT c
     * FROM Category c
     * WHERE c.name = :name
     */
    Optional<Category> findByName(String name);
}