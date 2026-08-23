package sg.edu.nus.empdemo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import sg.edu.nus.empdemo.model.Employee;
import sg.edu.nus.empdemo.model.Project;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void shouldFindProjectByExactName() {

        // Arrange
        Project project = new Project(
                "AI Platform",
                "Enterprise AI Platform",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        entityManager.persistAndFlush(project);
        entityManager.clear();

        // Act
        List<Project> result =
                projectRepository
                        .findByName("AI Platform");

        // Assert
        assertThat(result).hasSize(1);

        assertThat(result.get(0).getName())
                .isEqualTo("AI Platform");
    }

    @Test
    void shouldFindProjectByPartialNameIgnoringCase() {

        // Arrange
        Project project = new Project(
                "AI Platform",
                "Enterprise AI Platform",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        entityManager.persistAndFlush(project);
        entityManager.clear();

        // Act
        List<Project> result =
                projectRepository
                        .findByNameContainingIgnoreCase(
                                "platform");

        // Assert
        assertThat(result).hasSize(1);

        assertThat(result.get(0).getName())
                .isEqualTo("AI Platform");
    }

    @Test
    void shouldFindProjectsEndingAfterDate() {

        // Arrange
        Project project1 = new Project(
                "AI Platform",
                "AI",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        Project project2 = new Project(
                "Web Portal",
                "Web",
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 9, 30));

        Project project3 = new Project(
                "Legacy Migration",
                "Legacy",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31));

        entityManager.persist(project1);
        entityManager.persist(project2);
        entityManager.persist(project3);

        entityManager.flush();
        entityManager.clear();

        // Act
        List<Project> result =
                projectRepository.findByEndDateAfter(
                        LocalDate.of(2026, 6, 1));

        // Assert
        assertThat(result)
                .extracting(Project::getName)
                .containsExactlyInAnyOrder(
                        "AI Platform",
                        "Web Portal");
    }

    @Test
    void shouldFindProjectsWithinDateRange() {

        // Arrange
        Project project1 = new Project(
                "AI Platform",
                "AI",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        Project project2 = new Project(
                "Web Portal",
                "Web",
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 9, 30));

        Project project3 = new Project(
                "Legacy Migration",
                "Legacy",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31));

        entityManager.persist(project1);
        entityManager.persist(project2);
        entityManager.persist(project3);

        entityManager.flush();
        entityManager.clear();

        // Act
        List<Project> result =
                projectRepository.findByDateRange(
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 12, 31));

        // Assert
        assertThat(result)
                .extracting(Project::getName)
                .containsExactlyInAnyOrder(
                        "AI Platform",
                        "Web Portal");
    }

    @Test
    void shouldFetchProjectWithEmployees() {

        // Arrange
        Project project = new Project(
                "AI Platform",
                "Enterprise AI Platform",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        entityManager.persistAndFlush(project);

        Employee alice =
                new Employee("Alice Tan");

        Employee bob =
                new Employee("Bob Lim");

        alice.addProject(project);
        bob.addProject(project);

        entityManager.persist(alice);
        entityManager.persist(bob);

        entityManager.flush();

        Long projectId = project.getId();

        entityManager.clear();

        // Act
        Optional<Project> result =
                projectRepository
                        .findByIdWithEmployees(projectId);

        // Assert
        assertThat(result).isPresent();

        assertThat(result.get().getEmployees())
                .hasSize(2);

        assertThat(result.get().getEmployees())
                .extracting(Employee::getName)
                .containsExactlyInAnyOrder(
                        "Alice Tan",
                        "Bob Lim");
    }

    @Test
    void shouldFindProjectsByEmployeeId() {

        // Arrange
        Project project1 = new Project(
                "AI Platform",
                "AI",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        Project project2 = new Project(
                "Web Portal",
                "Web",
                LocalDate.of(2026, 2, 1),
                LocalDate.of(2026, 9, 30));

        Employee employee =
                new Employee("Alice Tan");

        employee.addProject(project1);
        employee.addProject(project2);

        entityManager.persistAndFlush(employee);

        Long employeeId = employee.getId();

        entityManager.clear();

        // Act
        List<Project> result =
                projectRepository
                        .findByEmployeesId(employeeId);

        // Assert
        assertThat(result).hasSize(2);

        assertThat(result)
                .extracting(Project::getName)
                .containsExactlyInAnyOrder(
                        "AI Platform",
                        "Web Portal");
    }
}
