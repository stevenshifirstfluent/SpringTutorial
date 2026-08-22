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
import sg.edu.nus.empdemo.model.Employee;
import sg.edu.nus.empdemo.model.Project;

@DataJpaTest
class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager entityManager;

    private Project aiProject;
    private Project webProject;
    private Project legacyProject;

    private Employee alice;
    private Employee bob;

    @BeforeEach
    void setUp() {

        aiProject = new Project(
                "AI Platform",
                "Enterprise AI Platform",
                LocalDate.of(2026, 1, 1),
                LocalDate.of(2026, 12, 31));

        webProject = new Project(
                "Web Portal",
                "Employee Web Portal",
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 9, 30));

        legacyProject = new Project(
                "Legacy Migration",
                "Legacy system migration",
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31));

        alice = new Employee("Alice Tan");
        bob = new Employee("Bob Lim");

        alice.addProject(aiProject);
        alice.addProject(webProject);

        bob.addProject(aiProject);

        employeeRepository.save(alice);
        employeeRepository.save(bob);

        projectRepository.save(legacyProject);

        employeeRepository.flush();

        entityManager.clear();
    }

    @Test
    void shouldFindProjectByExactName() {

        List<Project> projects =
                projectRepository.findByName("AI Platform");

        assertThat(projects).hasSize(1);

        assertThat(projects.get(0).getName())
                .isEqualTo("AI Platform");
    }

    @Test
    void shouldFindProjectByPartialNameIgnoringCase() {

        List<Project> projects =
                projectRepository
                        .findByNameContainingIgnoreCase("platform");

        assertThat(projects).hasSize(1);

        assertThat(projects.get(0).getName())
                .isEqualTo("AI Platform");
    }

    @Test
    void shouldFindProjectsEndingAfterDate() {

        List<Project> projects =
                projectRepository.findByEndDateAfter(
                        LocalDate.of(2026, 6, 1));

        assertThat(projects)
                .extracting(Project::getName)
                .containsExactlyInAnyOrder(
                        "AI Platform",
                        "Web Portal");
    }

    @Test
    void shouldFindProjectsWithinDateRange() {

        List<Project> projects =
                projectRepository.findByDateRange(
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 12, 31));

        assertThat(projects)
                .extracting(Project::getName)
                .containsExactlyInAnyOrder(
                        "AI Platform",
                        "Web Portal");
    }

    @Test
    void shouldFetchProjectWithEmployees() {

        Optional<Project> result =
                projectRepository.findByIdWithEmployees(
                        aiProject.getId());

        assertThat(result).isPresent();

        Project project = result.get();

        assertThat(project.getEmployees()).hasSize(2);

        assertThat(project.getEmployees())
                .extracting(Employee::getName)
                .containsExactlyInAnyOrder(
                        "Alice Tan",
                        "Bob Lim");
    }

    @Test
    void shouldFindProjectsByEmployeeId() {

        List<Project> projects =
                projectRepository.findByEmployeesId(
                        alice.getId());

        assertThat(projects).hasSize(2);

        assertThat(projects)
                .extracting(Project::getName)
                .containsExactlyInAnyOrder(
                        "AI Platform",
                        "Web Portal");
    }
}