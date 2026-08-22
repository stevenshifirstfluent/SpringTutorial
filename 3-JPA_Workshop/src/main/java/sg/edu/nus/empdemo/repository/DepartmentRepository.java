package sg.edu.nus.empdemo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.edu.nus.empdemo.model.Department;

@Repository
public interface DepartmentRepository
        extends JpaRepository<Department, Long> {

    Optional<Department> findByName(String name);

    List<Department> findByNameContainingIgnoreCase(String name);

    @Query("""
        SELECT d
        FROM Department d
        LEFT JOIN FETCH d.employee
        WHERE d.id = :id
        """)
    Optional<Department> findByIdWithEmployee(@Param("id") Long id);

    @Query("""
        SELECT CASE
            WHEN COUNT(e) > 0 THEN true
            ELSE false
        END
        FROM Department d
        LEFT JOIN d.employee e
        WHERE d.id = :departmentId
        """)
    boolean hasEmployee(
            @Param("departmentId") Long departmentId);
}