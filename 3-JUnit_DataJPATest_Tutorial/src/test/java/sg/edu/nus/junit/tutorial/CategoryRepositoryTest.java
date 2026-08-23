package sg.edu.nus.junit.tutorial;


import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * @DataJpaTest is used to test the JPA persistence layer.
 *
 * Spring Boot will load:
 * - JPA configuration
 * - Entity classes
 * - Spring Data repositories
 * - EntityManager
 * - Test database
 *
 * It does NOT load the whole application such as:
 * - Controllers
 * - Web server
 * - Most service components
 *
 * By default, each test runs inside a transaction.
 * The transaction is rolled back after the test finishes.
 */
@DataJpaTest
class CategoryRepositoryTest {

    /*
     * TestEntityManager is a testing-friendly wrapper
     * around JPA EntityManager.
     *
     * We use it to prepare test data independently
     * from CategoryRepository.
     */
    @Autowired
    private TestEntityManager entityManager;

    /*
     * This is the repository that we want to test.
     *
     * Spring Data JPA automatically creates the
     * implementation and injects it here.
     */
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByName_shouldReturnMatchingCategory() {

        /*
         * ==================================================
         * ARRANGE
         * ==================================================
         *
         * Prepare the database state required by the test.
         */

        Category category =
                new Category("Kitchen Knives");

        /*
         * persistAndFlush() performs two important operations:
         *
         * 1. persist()
         *    The Category becomes a managed JPA entity.
         *
         * 2. flush()
         *    Hibernate synchronizes the persistence context
         *    with the database.
         *
         * Hibernate will execute SQL similar to:
         *
         * INSERT INTO category (name)
         * VALUES ('Kitchen Knives');
         *
         * Important:
         * flush() does NOT mean commit().
         *
         * @DataJpaTest normally rolls back the transaction
         * when the test finishes.
         */
        entityManager.persistAndFlush(category);

        /*
         * clear() removes managed entities from the
         * persistence context / first-level cache.
         *
         * Why do this?
         *
         * We want the repository to retrieve the Category
         * from the database rather than relying on an entity
         * that is already managed in memory.
         */
        entityManager.clear();

        /*
         * ==================================================
         * ACT
         * ==================================================
         *
         * Execute the repository method being tested.
         */

        Optional<Category> result =
                categoryRepository.findByName("Kitchen Knives");

        /*
         * ==================================================
         * ASSERT
         * ==================================================
         *
         * Verify that the repository returned
         * the expected result.
         */

        assertThat(result).isPresent();

        assertThat(result.get().getName())
                .isEqualTo("Kitchen Knives");
    }


    @Test
    void findByName_shouldReturnEmpty_whenCategoryDoesNotExist() {

        /*
         * No Category is inserted into the database.
         *
         * Therefore the query should not find anything.
         */

        // ACT
        Optional<Category> result =
                categoryRepository.findByName("Unknown Category");

        // ASSERT
        assertThat(result).isEmpty();
    }
}