package sg.edu.nus.empdemo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import sg.edu.nus.empdemo.model.Department;
import sg.edu.nus.empdemo.model.Employee;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void shouldFindDepartmentByExactName() {

        // Arrange
        Department department =
                new Department("Information Technology");

        entityManager.persistAndFlush(department);
        entityManager.clear();

        // Act
        Optional<Department> result =
                departmentRepository.findByName(
                        "Information Technology");

        // Assert
        assertThat(result).isPresent();

        assertThat(result.get().getName())
                .isEqualTo("Information Technology");
    }

    @Test
    void shouldFindDepartmentByPartialNameIgnoringCase() {

        // Arrange
        Department department =
                new Department("Information Technology");

        entityManager.persistAndFlush(department);
        entityManager.clear();

        // Act
        List<Department> result =
                departmentRepository
                        .findByNameContainingIgnoreCase(
                                "technology");

        // Assert
        assertThat(result).hasSize(1);

        assertThat(result.get(0).getName())
                .isEqualTo("Information Technology");
    }

    @Test
    void shouldFetchDepartmentWithEmployee() {

        // Arrange
        Department department =
                new Department("Information Technology");

        entityManager.persistAndFlush(department);

        Employee employee =
                new Employee("Alice Tan");

        employee.assignDepartment(department);

        entityManager.persistAndFlush(employee);

        Long departmentId = department.getId();

        entityManager.clear();

        // Act
        Optional<Department> result =
                departmentRepository
                        .findByIdWithEmployee(departmentId);

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
    void shouldReturnTrueWhenDepartmentHasEmployee() {

        // Arrange
        Department department =
                new Department("IT");

        entityManager.persistAndFlush(department);

        Employee employee =
                new Employee("Alice");

        employee.assignDepartment(department);

        entityManager.persistAndFlush(employee);

        Long departmentId = department.getId();

        entityManager.clear();

        // Act
        boolean result =
                departmentRepository
                        .hasEmployee(departmentId);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenDepartmentHasNoEmployee() {

        // Arrange
        Department department =
                new Department("Finance");

        entityManager.persistAndFlush(department);

        Long departmentId = department.getId();

        entityManager.clear();

        // Act
        boolean result =
                departmentRepository
                        .hasEmployee(departmentId);

        // Assert
        assertThat(result).isFalse();
    }
}
