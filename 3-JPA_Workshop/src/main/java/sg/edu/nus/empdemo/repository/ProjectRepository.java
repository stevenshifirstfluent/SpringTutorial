package sg.edu.nus.empdemo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.edu.nus.empdemo.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByName(String name);

    List<Project> findByNameContainingIgnoreCase(String name);

    List<Project> findByEndDateAfter(LocalDate date);

    @Query("""
        SELECT p
        FROM Project p
        WHERE p.startDate >= :startDate
          AND p.endDate <= :endDate
        """)
    List<Project> findByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("""
        SELECT DISTINCT p
        FROM Project p
        LEFT JOIN FETCH p.employees
        WHERE p.id = :id
        """)
    Optional<Project> findByIdWithEmployees(@Param("id") Long id);

    List<Project> findByEmployeesId(Long employeeId);
}