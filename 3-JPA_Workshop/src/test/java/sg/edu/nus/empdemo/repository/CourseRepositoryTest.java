package sg.edu.nus.empdemo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import jakarta.persistence.EntityManager;
import sg.edu.nus.empdemo.model.Course;
import sg.edu.nus.empdemo.model.Employee;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager entityManager;

    private Employee alice;
    private Employee bob;

    private Course springBoot;
    private Course springJpa;
    private Course python;

    @BeforeEach
    void setUp() {

        alice = new Employee("Alice Tan");
        bob = new Employee("Bob Lim");

        springBoot = new Course(
                "Spring Boot",
                2.0,
                LocalDate.of(2026, 3, 1));

        springJpa = new Course(
                "Spring Data JPA",
                1.5,
                LocalDate.of(2026, 5, 1));

        python = new Course(
                "Python Programming",
                3.0,
                LocalDate.of(2026, 2, 1));

        alice.addCourse(springBoot);
        alice.addCourse(springJpa);

        bob.addCourse(python);

        employeeRepository.save(alice);
        employeeRepository.save(bob);

        employeeRepository.flush();

        entityManager.clear();
    }

    @Test
    void shouldFindCourseByPartialNameIgnoringCase() {

        List<Course> courses =
                courseRepository
                        .findByNameContainingIgnoreCase("spring");

        assertThat(courses).hasSize(2);

        assertThat(courses)
                .extracting(Course::getName)
                .containsExactlyInAnyOrder(
                        "Spring Boot",
                        "Spring Data JPA");
    }

    @Test
    void shouldFindCoursesStartingAfterDate() {

        List<Course> courses =
                courseRepository.findByStartsAfter(
                        LocalDate.of(2026, 3, 15));

        assertThat(courses)
                .extracting(Course::getName)
                .containsExactly("Spring Data JPA");
    }

    @Test
    void shouldFindCoursesByMaximumDuration() {

        List<Course> courses =
                courseRepository
                        .findByDurationInMonthsLessThanEqual(2.0);

        assertThat(courses).hasSize(2);

        assertThat(courses)
                .extracting(Course::getName)
                .containsExactlyInAnyOrder(
                        "Spring Boot",
                        "Spring Data JPA");
    }

    @Test
    void shouldFindCoursesByEmployeeId() {

        List<Course> courses =
                courseRepository.findByEmployeeId(
                        alice.getId());

        assertThat(courses).hasSize(2);

        assertThat(courses)
                .extracting(Course::getName)
                .containsExactlyInAnyOrder(
                        "Spring Boot",
                        "Spring Data JPA");
    }

    @Test
    void shouldFetchCourseWithEmployee() {

        Optional<Course> result =
                courseRepository.findByIdWithEmployee(
                        springBoot.getId());

        assertThat(result).isPresent();

        Course course = result.get();

        assertThat(course.getEmployee()).isNotNull();

        assertThat(course.getEmployee().getName())
                .isEqualTo("Alice Tan");
    }

    @Test
    void shouldFindCoursesByEmployeeAndStartDate() {

        List<Course> courses =
                courseRepository
                        .findByEmployeeIdAndStartsAfter(
                                alice.getId(),
                                LocalDate.of(2026, 4, 1));

        assertThat(courses).hasSize(1);

        assertThat(courses.get(0).getName())
                .isEqualTo("Spring Data JPA");
    }
}