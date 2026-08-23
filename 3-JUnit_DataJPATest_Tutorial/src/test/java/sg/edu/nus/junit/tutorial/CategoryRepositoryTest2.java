package sg.edu.nus.junit.tutorial;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/*
 * @DataJpaTest
 * ------------
 * Loads only the components required for testing
 * the JPA persistence layer.
 *
 * It includes:
 * - JPA entities
 * - Spring Data repositories
 * - EntityManager
 * - Test database
 *
 * Each test normally runs inside a transaction.
 * Spring rolls the transaction back after the test.
 */
@DataJpaTest

/*
 * @DisplayName gives the test class a readable name
 * in the JUnit test report.
 */
@DisplayName("Category Repository Tests")
class CategoryRepositoryTest2 {


    /*
     * TestEntityManager is used to prepare test data
     * independently from the repository being tested.
     */
    @Autowired
    private TestEntityManager entityManager;


    /*
     * CategoryRepository is the component
     * that we want to test.
     */
    @Autowired
    private CategoryRepository categoryRepository;



    // =====================================================
    // @BeforeAll
    // =====================================================

    /*
     * Runs ONCE before all tests in this class.
     *
     * Because this method is static, we normally use it
     * for initialization that does NOT depend on
     * Spring-injected instance variables.
     */
    @BeforeAll
    static void initDatabase() {

        System.out.println(
                ">>> @BeforeAll - Starting CategoryRepository tests"
        );
    }



    // =====================================================
    // @BeforeEach
    // =====================================================

    /*
     * Runs before EACH test.
     *
     * We use it here to prepare a known database state
     * for every test.
     *
     * This helps keep tests:
     * - independent
     * - repeatable
     * - predictable
     */
    @BeforeEach
    void setUp() {

        System.out.println(
                ">>> @BeforeEach - Preparing test data"
        );


        Category category =
                new Category("Kitchen Knives");


        /*
         * persistAndFlush():
         *
         * persist()
         *     -> makes the entity managed by JPA
         *
         * flush()
         *     -> synchronizes the persistence context
         *        with the database
         *
         * Hibernate therefore executes an INSERT.
         *
         * Important:
         * flush() is NOT the same as commit().
         */
        entityManager.persistAndFlush(category);


        /*
         * Clear the persistence context.
         *
         * This removes managed entities from
         * JPA's first-level cache.
         *
         * Repository queries will therefore retrieve
         * the data from the database rather than using
         * the already-managed Category object.
         */
        entityManager.clear();
    }



    // =====================================================
    // Normal @Test
    // =====================================================

    @Test
    @DisplayName("Should find category by name")
    void shouldFindCategoryByName() {

        /*
         * ARRANGE
         *
         * Already performed by @BeforeEach.
         *
         * Database contains:
         *
         * Category("Kitchen Knives")
         */


        /*
         * ACT
         *
         * Execute the repository method
         * that we want to test.
         */
        Optional<Category> result =
                categoryRepository.findByName(
                        "Kitchen Knives"
                );


        /*
         * ASSERT
         *
         * Verify that a Category was found.
         */
        assertThat(result)
                .isPresent();


        /*
         * Also verify that the correct
         * Category was returned.
         */
        assertThat(result.get().getName())
                .isEqualTo("Kitchen Knives");
    }



    // =====================================================
    // Another normal @Test
    // =====================================================

    @Test
    @DisplayName("Should return empty when category does not exist")
    void shouldReturnEmptyWhenCategoryDoesNotExist() {

        // ACT
        Optional<Category> result =
                categoryRepository.findByName(
                        "Unknown Category"
                );


        // ASSERT
        assertThat(result)
                .isEmpty();
    }



    // =====================================================
    // @AfterEach
    // =====================================================

    /*
     * Runs after EACH test.
     *
     * With @DataJpaTest, Spring normally rolls back
     * the transaction automatically after a test.
     *
     * Therefore, we normally do NOT need to manually
     * delete the test data here.
     *
     * @AfterEach is still useful for demonstrating
     * test lifecycle behaviour or cleaning up
     * non-database resources.
     */
    @AfterEach
    void tearDown() {

        System.out.println(
                ">>> @AfterEach - Test completed"
        );
    }



    // =====================================================
    // @Disabled
    // =====================================================

    /*
     * @Disabled tells JUnit:
     *
     * "Do not execute this test."
     *
     * We should normally provide a reason explaining
     * why the test has temporarily been disabled.
     */
    @Test
    @Disabled("Skipped until issue #42 is resolved")
    @DisplayName("Should update category name")
    void skippedTest() {

        Category category =
                categoryRepository
                        .findByName("Kitchen Knives")
                        .orElseThrow();


        category.setName("Cooking Knives");


        assertThat(category.getName())
                .isEqualTo("Cooking Knives");
    }



    // =====================================================
    // @RepeatedTest
    // =====================================================

    /*
     * @RepeatedTest(3)
     *
     * Executes this test THREE times.
     *
     * @BeforeEach and @AfterEach are executed
     * for every repetition.
     *
     * Therefore:
     *
     * repetition 1
     *   -> @BeforeEach
     *   -> test
     *   -> @AfterEach
     *
     * repetition 2
     *   -> @BeforeEach
     *   -> test
     *   -> @AfterEach
     *
     * repetition 3
     *   -> @BeforeEach
     *   -> test
     *   -> @AfterEach
     */
    @RepeatedTest(3)
    @DisplayName("Should repeatedly find category")
    void repeatMe() {

        Optional<Category> result =
                categoryRepository.findByName(
                        "Kitchen Knives"
                );


        assertThat(result)
                .isPresent();
    }



    // =====================================================
    // @AfterAll
    // =====================================================

    /*
     * Runs ONCE after every test in this class
     * has completed.
     *
     * Like @BeforeAll, this method is static
     * by default.
     */
    @AfterAll
    static void cleanUp() {

        System.out.println(
                ">>> @AfterAll - CategoryRepository tests completed"
        );
    }
}
