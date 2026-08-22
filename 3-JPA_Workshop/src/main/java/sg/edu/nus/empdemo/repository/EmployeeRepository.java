package sg.edu.nus.empdemo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.edu.nus.empdemo.model.Employee;

@Repository
public interface EmployeeRepository
        extends JpaRepository<Employee, Long> {

    List<Employee> findByNameContainingIgnoreCase(String name);

    @Query("""
        SELECT e
        FROM Employee e
        LEFT JOIN FETCH e.department
        WHERE e.id = :id
        """)
    Optional<Employee> findByIdWithDepartment(@Param("id") Long id);

    List<Employee> findByDepartmentId(Long departmentId);

    @Query("""
        SELECT DISTINCT e
        FROM Employee e
        LEFT JOIN FETCH e.projects
        WHERE e.id = :id
        """)
    Optional<Employee> findByIdWithProjects(@Param("id") Long id);

    List<Employee> findByProjectsId(Long projectId);

    @Query("""
        SELECT DISTINCT e
        FROM Employee e
        LEFT JOIN FETCH e.courses
        WHERE e.id = :id
        """)
    Optional<Employee> findByIdWithCourses(@Param("id") Long id);
}