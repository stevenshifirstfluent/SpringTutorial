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
import sg.edu.nus.empdemo.model.Department;
import sg.edu.nus.empdemo.model.Employee;
import sg.edu.nus.empdemo.model.Project;

@DataJpaTest
class EmployeeRepositoryTest {

	@Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManager entityManager;

    private Employee alice;
    private Employee bob;

    private Department itDepartment;
    private Department hrDepartment;

    private Project aiProject;
    private Project webProject;

    @BeforeEach
    void setUp() {

        itDepartment = new Department("Information Technology");
        hrDepartment = new Department("Human Resources");

        departmentRepository.save(itDepartment);
        departmentRepository.save(hrDepartment);

        aiProject = new Project(
                "AI Platform",
                "Enterprise AI Platform",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        webProject = new Project(
                "Web Portal",
                "Employee Web Portal",
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 8, 31));

        alice = new Employee("Alice Tan");
        alice.assignDepartment(itDepartment);

        alice.addProject(aiProject);
        alice.addProject(webProject);

        alice.addCourse(
                new Course(
                        "Spring Boot",
                        2.0,
                        LocalDate.of(2026, 3, 1)));

        alice.addCourse(
                new Course(
                        "Spring Data JPA",
                        1.5,
                        LocalDate.of(2026, 4, 1)));

        bob = new Employee("Bob Lim");
        bob.assignDepartment(hrDepartment);

        bob.addProject(aiProject);

        employeeRepository.save(alice);
        employeeRepository.save(bob);

        employeeRepository.flush();

        entityManager.clear();
    }

    @Test
    void shouldFindEmployeeByPartialNameIgnoringCase() {

        List<Employee> employees =
                employeeRepository.findByNameContainingIgnoreCase("alice");

        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getName())
                .isEqualTo("Alice Tan");
    }

    @Test
    void shouldFindEmployeeByDepartmentId() {

        List<Employee> employees =
                employeeRepository.findByDepartmentId(
                        itDepartment.getId());

        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getName())
                .isEqualTo("Alice Tan");
    }

    @Test
    void shouldFetchEmployeeWithDepartment() {

        Optional<Employee> result =
                employeeRepository.findByIdWithDepartment(
                        alice.getId());

        assertThat(result).isPresent();

        Employee employee = result.get();

        assertThat(employee.getDepartment()).isNotNull();
        assertThat(employee.getDepartment().getName())
                .isEqualTo("Information Technology");
    }

    @Test
    void shouldFetchEmployeeWithProjects() {

        Optional<Employee> result =
                employeeRepository.findByIdWithProjects(
                        alice.getId());

        assertThat(result).isPresent();

        Employee employee = result.get();

        assertThat(employee.getProjects())
                .hasSize(2);

        assertThat(employee.getProjects())
                .extracting(Project::getName)
                .containsExactlyInAnyOrder(
                        "AI Platform",
                        "Web Portal");
    }

    @Test
    void shouldFindEmployeesByProjectId() {

        List<Employee> employees =
                employeeRepository.findByProjectsId(
                        aiProject.getId());

        assertThat(employees).hasSize(2);

        assertThat(employees)
                .extracting(Employee::getName)
                .containsExactlyInAnyOrder(
                        "Alice Tan",
                        "Bob Lim");
    }

    @Test
    void shouldFetchEmployeeWithCourses() {

        Optional<Employee> result =
                employeeRepository.findByIdWithCourses(
                        alice.getId());

        assertThat(result).isPresent();

        Employee employee = result.get();

        assertThat(employee.getCourses())
                .hasSize(2);

        assertThat(employee.getCourses())
                .extracting(Course::getName)
                .containsExactlyInAnyOrder(
                        "Spring Boot",
                        "Spring Data JPA");
    }
    
    @Test
    void deletingEmployeeShouldDeleteCourses() {

        Employee employee = new Employee("Alice");

        Course course = new Course(
                "Spring Boot",
                2.0,
                LocalDate.of(2026, 9, 1));

        employee.addCourse(course);

        employeeRepository.saveAndFlush(employee);

        Long courseId = course.getId();

        employeeRepository.delete(employee);
        employeeRepository.flush();

        entityManager.clear();

        assertThat(courseRepository.findById(courseId))
                .isEmpty();
    }
    
    @Test
    void removeCourseShouldSynchronizeBothSides() {

        Employee employee = new Employee("Alice");

        Course course = new Course(
                "Spring Boot",
                2.0,
                LocalDate.of(2026, 9, 1));

        employee.addCourse(course);

        employee.removeCourse(course);

        assertThat(employee.getCourses())
                .doesNotContain(course);

        assertThat(course.getEmployee())
                .isNull();
    }
    
    @Test
    void removeProjectShouldSynchronizeBothSides() {

        Employee employee = new Employee("Alice");

        Project project = new Project(
                "AI Project",
                "Test project",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        employee.addProject(project);

        employee.removeProject(project);

        assertThat(employee.getProjects())
                .doesNotContain(project);

        assertThat(project.getEmployees())
                .doesNotContain(employee);
    }
}