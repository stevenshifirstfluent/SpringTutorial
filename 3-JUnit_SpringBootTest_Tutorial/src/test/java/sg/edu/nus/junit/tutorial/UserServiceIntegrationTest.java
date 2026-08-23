package sg.edu.nus.junit.tutorial;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import java.util.List;

@SpringBootTest

/*
 * Activates application-test.properties.
 *
 * This allows us to use test-specific configuration
 * instead of the normal application configuration.
 */
@ActiveProfiles("test")

/*
 * Overrides selected Spring properties specifically
 * for this test class.
 *
 * Here we force Spring Boot to use an H2
 * in-memory database.
 */
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
/*
 * Enable JUnit test execution order.
 *
 * @Order(1), @Order(2), etc. only take effect
 * when a MethodOrderer is configured for the class.
 *
 * We use ordering here only for demonstration:
 *
 * Test 1 commits data
 * Test 2 verifies that the committed data still exists
 *
 * In normal projects, tests should preferably
 * remain independent from each other.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceIntegrationTest {

    /*
     * Real UserService bean from the Spring context.
     *
     * Unlike @DataJpaTest, @SpringBootTest loads
     * the full application context.
     */
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;


    /*
     * Replace the real EmailService bean with a Mockito mock.
     *
     * Why?
     *
     * During integration testing we may not want to:
     *
     * - send a real email
     * - call an external API
     * - consume external resources
     *
     * The rest of the application still uses EmailService,
     * but Spring injects this mock instead.
     */
    @MockitoBean
    private EmailService emailService;


    /*
     * Keep the REAL AuditService bean,
     * but wrap it with a Mockito spy.
     *
     * This means:
     *
     * real method executes
     * +
     * we can verify that it was called
     */
    @MockitoSpyBean
    private AuditService auditService;


    @Test

    /*
     * Execute SQL BEFORE the test method.
     *
     * Spring will load:
     *
     * src/test/resources/test-data/users.sql
     * Test just to 
     */
    @Sql("/test-data/users.sql")

    /*
     * Execute cleanup SQL AFTER this test method.
     */
    @Sql(
            scripts = "/test-data/cleanup.sql",
            executionPhase = ExecutionPhase.AFTER_TEST_METHOD
    )
    void shouldCreateUser() {

        // ==============================
        // ACT
        // ==============================

        User created =
                userService.create(
                        new User("Alice")
                );


        // ==============================
        // ASSERT
        // ==============================

        assertThat(created.getId())
                .isNotNull();

        assertThat(created.getName())
                .isEqualTo("Alice");


        /*
         * EmailService is mocked.
         *
         * No real email is sent.
         *
         * But we can verify that UserService
         * attempted to send one.
         */
        verify(emailService)
                .sendWelcomeEmail(created);


        /*
         * AuditService is a spy.
         *
         * The real method executed,
         * and we can also verify the interaction.
         */
        verify(auditService)
                .recordUserCreated(created);
    }


    @Test
    @Transactional

    /*
     * By default, a @Transactional Spring test
     * is rolled back after the test finishes.
     *
     * @Rollback(false) tells Spring NOT to roll
     * the transaction back.
     *
     * Therefore, the inserted User is committed
     * to the database when this test completes.
     *
     * This is used here only to demonstrate
     * the difference between COMMIT and ROLLBACK.
     */
    @Rollback(false)

    /*
     * Run this test before the @Order(2) test.
     *
     * This allows the next test to check whether
     * the committed data is still available.
     */
    @Order(1)
    void shouldPersistAndCommit() {

        // ACT
        User created =
                userService.create(
                        new User("Committed User")
                );

        // ASSERT
        // An ID confirms that the entity was persisted.
        assertThat(created.getId())
                .isNotNull();
    }
    
    @Test
    /*
     * Run this test after shouldPersistAndCommit().
     *
     * This test does NOT create the user itself.
     * It reads the database again to verify that
     * the previous transaction was really committed.
     */
    @Order(2)
    void shouldVerifyCommittedUserExists() {

        /*
         * Query the database again.
         *
         * If Test 1 was committed successfully,
         * "Committed User" should still exist.
         *
         * If Test 1 had been rolled back,
         * findAll() would return no such user.
         */
        List<User> users =
                userRepository.findAll();

        /*
         * Extract the name from every User
         * and verify that the committed user
         * is present in the database.
         */
        assertThat(users)
                .extracting(User::getName)
                .contains("Committed User");
    }
}
