package sg.edu.nus.empdemo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import jakarta.persistence.EntityManager;
import sg.edu.nus.empdemo.model.Department;
import sg.edu.nus.empdemo.model.Employee;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager entityManager;

    private Department itDepartment;
    private Department financeDepartment;

    private Employee alice;

    @BeforeEach
    void setUp() {

        itDepartment =
                new Department("Information Technology");

        financeDepartment =
                new Department("Finance");

        departmentRepository.save(itDepartment);
        departmentRepository.save(financeDepartment);

        alice = new Employee("Alice Tan");

        alice.assignDepartment(itDepartment);

        employeeRepository.save(alice);

        employeeRepository.flush();

        entityManager.clear();
    }

    @Test
    void shouldFindDepartmentByExactName() {

        Optional<Department> result =
                departmentRepository.findByName(
                        "Information Technology");

        assertThat(result).isPresent();

        assertThat(result.get().getName())
                .isEqualTo("Information Technology");
    }

    @Test
    void shouldFindDepartmentByPartialNameIgnoringCase() {

        List<Department> departments =
                departmentRepository
                        .findByNameContainingIgnoreCase("technology");

        assertThat(departments).hasSize(1);

        assertThat(departments.get(0).getName())
                .isEqualTo("Information Technology");
    }

    @Test
    void shouldFetchDepartmentWithEmployee() {

        Optional<Department> result =
                departmentRepository.findByIdWithEmployee(
                        itDepartment.getId());

        assertThat(result).isPresent();

        Department department = result.get();

        assertThat(department.getEmployee()).isNotNull();

        assertThat(department.getEmployee().getName())
                .isEqualTo("Alice Tan");
    }

    @Test
    void shouldReturnTrueWhenDepartmentHasEmployee() {

        boolean result =
                departmentRepository.hasEmployee(
                        itDepartment.getId());

        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenDepartmentHasNoEmployee() {

        boolean result =
                departmentRepository.hasEmployee(
                        financeDepartment.getId());

        assertThat(result).isFalse();
    }
}