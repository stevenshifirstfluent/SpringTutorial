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
import sg.edu.nus.empdemo.model.Department;
import sg.edu.nus.empdemo.model.Employee;
import sg.edu.nus.empdemo.model.Project;

@DataJpaTest
class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // ---------------------------------------------------------
    // Derived query:
    // findByNameContainingIgnoreCase
    // ---------------------------------------------------------

    @Test
    void shouldFindEmployeeByPartialNameIgnoringCase() {

        // Arrange
        Employee employee = new Employee("Alice Tan");

        entityManager.persistAndFlush(employee);
        entityManager.clear();

        // Act
        List<Employee> result =
                employeeRepository
                        .findByNameContainingIgnoreCase("alice");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName())
                .isEqualTo("Alice Tan");
    }

    // ---------------------------------------------------------
    // Employee -> Department
    // ---------------------------------------------------------

    @Test
    void shouldFindEmployeeByDepartmentId() {

        // Arrange
        Department department =
                new Department("Information Technology");

        /*
         * Employee does not cascade persist to Department,
         * so Department must be persisted first.
         */
        entityManager.persistAndFlush(department);

        Employee employee = new Employee("Alice Tan");
        employee.assignDepartment(department);

        entityManager.persistAndFlush(employee);
        entityManager.clear();

        // Act
        List<Employee> result =
                employeeRepository
                        .findByDepartmentId(department.getId());

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName())
                .isEqualTo("Alice Tan");
    }

    @Test
    void shouldFetchEmployeeWithDepartment() {

        // Arrange
        Department department =
                new Department("Information Technology");

        entityManager.persistAndFlush(department);

        Employee employee = new Employee("Alice Tan");
        employee.assignDepartment(department);

        entityManager.persistAndFlush(employee);

        Long employeeId = employee.getId();

        entityManager.clear();

        // Act
        Optional<Employee> result =
                employeeRepository
                        .findByIdWithDepartment(employeeId);

        // Assert
        assertThat(result).isPresent();

        assertThat(result.get().getDepartment())
                .isNotNull();

        assertThat(result.get().getDepartment().getName())
                .isEqualTo("Information Technology");
    }

    // ---------------------------------------------------------
    // Employee <-> Project
    // ---------------------------------------------------------

    @Test
    void shouldFetchEmployeeWithProjects() {

        // Arrange
        Project project1 = new Project(
                "AI Platform",
                "Enterprise AI Platform",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        Project project2 = new Project(
                "Web Portal",
                "Employee Web Portal",
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 8, 31));

        Employee employee = new Employee("Alice Tan");

        employee.addProject(project1);
        employee.addProject(project2);

        /*
         * Employee -> Project uses
         * CascadeType.PERSIST and MERGE.
         *
         * Persisting Employee therefore also persists
         * the two new Project objects.
         */
        entityManager.persistAndFlush(employee);

        Long employeeId = employee.getId();

        entityManager.clear();

        // Act
        Optional<Employee> result =
                employeeRepository
                        .findByIdWithProjects(employeeId);

        // Assert
        assertThat(result).isPresent();

        assertThat(result.get().getProjects())
                .hasSize(2);

        assertThat(result.get().getProjects())
                .extracting(Project::getName)
                .containsExactlyInAnyOrder(
                        "AI Platform",
                        "Web Portal");
    }

    @Test
    void shouldFindEmployeesByProjectId() {

        // Arrange
        Project project = new Project(
                "AI Platform",
                "Enterprise AI Platform",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        /*
         * Persist the shared Project first.
         */
        entityManager.persistAndFlush(project);

        Employee alice = new Employee("Alice Tan");
        Employee bob = new Employee("Bob Lim");

        alice.addProject(project);
        bob.addProject(project);

        entityManager.persist(alice);
        entityManager.persist(bob);

        entityManager.flush();

        Long projectId = project.getId();

        entityManager.clear();

        // Act
        List<Employee> result =
                employeeRepository
                        .findByProjectsId(projectId);

        // Assert
        assertThat(result).hasSize(2);

        assertThat(result)
                .extracting(Employee::getName)
                .containsExactlyInAnyOrder(
                        "Alice Tan",
                        "Bob Lim");
    }

    // ---------------------------------------------------------
    // Employee -> Course
    // ---------------------------------------------------------

    @Test
    void shouldFetchEmployeeWithCourses() {

        // Arrange
        Employee employee =
                new Employee("Alice Tan");

        Course course1 = new Course(
                "Spring Boot",
                2.0,
                LocalDate.of(2026, 3, 1));

        Course course2 = new Course(
                "Spring Data JPA",
                1.5,
                LocalDate.of(2026, 4, 1));

        employee.addCourse(course1);
        employee.addCourse(course2);

        /*
         * Employee -> Course uses CascadeType.ALL,
         * therefore persisting Employee also persists Courses.
         */
        entityManager.persistAndFlush(employee);

        Long employeeId = employee.getId();

        entityManager.clear();

        // Act
        Optional<Employee> result =
                employeeRepository
                        .findByIdWithCourses(employeeId);

        // Assert
        assertThat(result).isPresent();

        assertThat(result.get().getCourses())
                .hasSize(2);

        assertThat(result.get().getCourses())
                .extracting(Course::getName)
                .containsExactlyInAnyOrder(
                        "Spring Boot",
                        "Spring Data JPA");
    }

    // ---------------------------------------------------------
    // Helper method tests
    // ---------------------------------------------------------

    @Test
    void assignDepartmentShouldSynchronizeBothSides() {

        // Arrange
        Employee employee =
                new Employee("Alice");

        Department department =
                new Department("IT");

        // Act
        employee.assignDepartment(department);

        // Assert
        assertThat(employee.getDepartment())
                .isEqualTo(department);

        assertThat(department.getEmployee())
                .isEqualTo(employee);
    }

    @Test
    void addProjectShouldSynchronizeBothSides() {

        // Arrange
        Employee employee =
                new Employee("Alice");

        Project project = new Project(
                "AI Project",
                "Test project",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        // Act
        employee.addProject(project);

        // Assert
        assertThat(employee.getProjects())
                .contains(project);

        assertThat(project.getEmployees())
                .contains(employee);
    }

    @Test
    void removeProjectShouldSynchronizeBothSides() {

        // Arrange
        Employee employee =
                new Employee("Alice");

        Project project = new Project(
                "AI Project",
                "Test project",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        employee.addProject(project);

        // Act
        employee.removeProject(project);

        // Assert
        assertThat(employee.getProjects())
                .doesNotContain(project);

        assertThat(project.getEmployees())
                .doesNotContain(employee);
    }

    @Test
    void addCourseShouldSynchronizeBothSides() {

        // Arrange
        Employee employee =
                new Employee("Alice");

        Course course = new Course(
                "Spring Boot",
                2.0,
                LocalDate.of(2026, 9, 1));

        // Act
        employee.addCourse(course);

        // Assert
        assertThat(employee.getCourses())
                .contains(course);

        assertThat(course.getEmployee())
                .isEqualTo(employee);
    }

    @Test
    void removeCourseShouldSynchronizeBothSides() {

        // Arrange
        Employee employee =
                new Employee("Alice");

        Course course = new Course(
                "Spring Boot",
                2.0,
                LocalDate.of(2026, 9, 1));

        employee.addCourse(course);

        // Act
        employee.removeCourse(course);

        // Assert
        assertThat(employee.getCourses())
                .doesNotContain(course);

        assertThat(course.getEmployee())
                .isNull();
    }

    // ---------------------------------------------------------
    // Cascade tests
    // ---------------------------------------------------------

    @Test
    void shouldCascadePersistCoursesWhenEmployeeIsPersisted() {

        // Arrange
        Employee employee =
                new Employee("Charlie Wong");

        Course course = new Course(
                "Hibernate",
                2.0,
                LocalDate.of(2026, 6, 1));

        employee.addCourse(course);

        // Act
        entityManager.persistAndFlush(employee);

        Long employeeId = employee.getId();

        entityManager.clear();

        // Assert
        Optional<Employee> result =
                employeeRepository
                        .findByIdWithCourses(employeeId);

        assertThat(result).isPresent();

        assertThat(result.get().getCourses())
                .hasSize(1);

        assertThat(result.get()
                .getCourses()
                .get(0)
                .getName())
                .isEqualTo("Hibernate");
    }

    @Test
    void deletingEmployeeShouldDeleteCourses() {

        // Arrange
        Employee employee =
                new Employee("Alice");

        Course course = new Course(
                "Spring Boot",
                2.0,
                LocalDate.of(2026, 9, 1));

        employee.addCourse(course);

        entityManager.persistAndFlush(employee);

        Long courseId = course.getId();

        entityManager.clear();

        // Act
        Employee persistedEmployee =
                entityManager.find(
                        Employee.class,
                        employee.getId());

        entityManager.remove(persistedEmployee);
        entityManager.flush();
        entityManager.clear();

        // Assert
        assertThat(courseRepository.findById(courseId))
                .isEmpty();
    }

    @Test
    void shouldDeleteCourseWhenRemovedFromEmployee() {

        // Arrange
        Employee employee =
                new Employee("David Tan");

        Course course = new Course(
                "Java Fundamentals",
                1.0,
                LocalDate.of(2026, 7, 1));

        employee.addCourse(course);

        entityManager.persistAndFlush(employee);

        Long employeeId = employee.getId();
        Long courseId = course.getId();

        entityManager.clear();

        Employee persistedEmployee =
                entityManager.find(
                        Employee.class,
                        employeeId);

        Course persistedCourse =
                persistedEmployee
                        .getCourses()
                        .get(0);

        // Act
        persistedEmployee.removeCourse(persistedCourse);

        entityManager.flush();
        entityManager.clear();

        // Assert
        assertThat(courseRepository.findById(courseId))
                .isEmpty();
    }

    @Test
    void shouldNotDeleteProjectWhenEmployeeIsDeleted() {

        // Arrange
        Employee employee =
                new Employee("Eric Lee");

        Project project = new Project(
                "Shared Platform",
                "Shared enterprise project",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        employee.addProject(project);

        entityManager.persistAndFlush(employee);

        Long employeeId = employee.getId();
        Long projectId = project.getId();

        entityManager.clear();

        // Act
        Employee persistedEmployee =
                entityManager.find(
                        Employee.class,
                        employeeId);

        entityManager.remove(persistedEmployee);
        entityManager.flush();
        entityManager.clear();

        // Assert
        assertThat(projectRepository.findById(projectId))
                .isPresent();
    }
}
