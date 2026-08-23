package sg.edu.nus.empdemo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import sg.edu.nus.empdemo.model.Course;
import sg.edu.nus.empdemo.model.Employee;

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void shouldFindCourseByPartialNameIgnoringCase() {

        // Arrange
        Course course1 = new Course(
                "Spring Boot",
                2.0,
                LocalDate.of(2026, 3, 1));

        Course course2 = new Course(
                "Spring Data JPA",
                1.5,
                LocalDate.of(2026, 5, 1));

        Course course3 = new Course(
                "Python Programming",
                3.0,
                LocalDate.of(2026, 2, 1));

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.persist(course3);

        entityManager.flush();
        entityManager.clear();

        // Act
        List<Course> result =
                courseRepository
                        .findByNameContainingIgnoreCase(
                                "spring");

        // Assert
        assertThat(result).hasSize(2);

        assertThat(result)
                .extracting(Course::getName)
                .containsExactlyInAnyOrder(
                        "Spring Boot",
                        "Spring Data JPA");
    }

    @Test
    void shouldFindCoursesStartingAfterDate() {

        // Arrange
        Course course1 = new Course(
                "Spring Boot",
                2.0,
                LocalDate.of(2026, 3, 1));

        Course course2 = new Course(
                "Spring Data JPA",
                1.5,
                LocalDate.of(2026, 5, 1));

        entityManager.persist(course1);
        entityManager.persist(course2);

        entityManager.flush();
        entityManager.clear();

        // Act
        List<Course> result =
                courseRepository.findByStartsAfter(
                        LocalDate.of(2026, 3, 15));

        // Assert
        assertThat(result).hasSize(1);

        assertThat(result.get(0).getName())
                .isEqualTo("Spring Data JPA");
    }

    @Test
    void shouldFindCoursesByMaximumDuration() {

        // Arrange
        Course course1 = new Course(
                "Spring Boot",
                2.0,
                LocalDate.of(2026, 3, 1));

        Course course2 = new Course(
                "Spring Data JPA",
                1.5,
                LocalDate.of(2026, 5, 1));

        Course course3 = new Course(
                "Python Programming",
                3.0,
                LocalDate.of(2026, 2, 1));

        entityManager.persist(course1);
        entityManager.persist(course2);
        entityManager.persist(course3);

        entityManager.flush();
        entityManager.clear();

        // Act
        List<Course> result =
                courseRepository
                        .findByDurationInMonthsLessThanEqual(
                                2.0);

        // Assert
        assertThat(result)
                .extracting(Course::getName)
                .containsExactlyInAnyOrder(
                        "Spring Boot",
                        "Spring Data JPA");
    }

    @Test
    void shouldFindCoursesByEmployeeId() {

        // Arrange
        Employee employee =
                new Employee("Alice Tan");

        entityManager.persistAndFlush(employee);

        Course course1 = new Course(
                "Spring Boot",
                2.0,
                LocalDate.of(2026, 3, 1));

        Course course2 = new Course(
                "Spring Data JPA",
                1.5,
                LocalDate.of(2026, 5, 1));

        course1.setEmployee(employee);
        course2.setEmployee(employee);

        entityManager.persist(course1);
        entityManager.persist(course2);

        entityManager.flush();

        Long employeeId = employee.getId();

        entityManager.clear();

        // Act
        List<Course> result =
                courseRepository
                        .findByEmployeeId(employeeId);

        // Assert
        assertThat(result).hasSize(2);

        assertThat(result)
                .extracting(Course::getName)
                .containsExactlyInAnyOrder(
                        "Spring Boot",
                        "Spring Data JPA");
    }

    @Test
    void shouldFetchCourseWithEmployee() {

        // Arrange
        Employee employee =
                new Employee("Alice Tan");

        entityManager.persistAndFlush(employee);

        Course course = new Course(
                "Spring Boot",
                2.0,
                LocalDate.of(2026, 3, 1));

        course.setEmployee(employee);

        entityManager.persistAndFlush(course);

        Long courseId = course.getId();

        entityManager.clear();

        // Act
        Optional<Course> result =
                courseRepository
                        .findByIdWithEmployee(courseId);

        // Assert
        assertThat(result).isPresent();

        assertThat(result.get().getEmployee())
                .isNotNull();

        assertThat(result.get()
                .getEmployee()
                .getName())
                .isEqualTo("Alice Tan");
    }

    @Test
    void shouldFindCoursesByEmployeeAndStartDate() {

        // Arrange
        Employee employee =
                new Employee("Alice Tan");

        entityManager.persistAndFlush(employee);

        Course course1 = new Course(
                "Spring Boot",
                2.0,
                LocalDate.of(2026, 3, 1));

        Course course2 = new Course(
                "Spring Data JPA",
                1.5,
                LocalDate.of(2026, 5, 1));

        course1.setEmployee(employee);
        course2.setEmployee(employee);

        entityManager.persist(course1);
        entityManager.persist(course2);

        entityManager.flush();

        Long employeeId = employee.getId();

        entityManager.clear();

        // Act
        List<Course> result =
                courseRepository
                        .findByEmployeeIdAndStartsAfter(
                                employeeId,
                                LocalDate.of(2026, 4, 1));

        // Assert
        assertThat(result).hasSize(1);

        assertThat(result.get(0).getName())
                .isEqualTo("Spring Data JPA");
    }
}
