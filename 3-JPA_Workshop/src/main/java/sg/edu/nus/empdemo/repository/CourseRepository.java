package sg.edu.nus.empdemo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sg.edu.nus.empdemo.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findByNameContainingIgnoreCase(String name);

    List<Course> findByStartsAfter(LocalDate date);

    List<Course> findByDurationInMonthsLessThanEqual(Double duration);

    List<Course> findByEmployeeId(Long employeeId);

    @Query("""
        SELECT c
        FROM Course c
        LEFT JOIN FETCH c.employee
        WHERE c.id = :id
        """)
    Optional<Course> findByIdWithEmployee(@Param("id") Long id);

    List<Course> findByEmployeeIdAndStartsAfter(
            Long employeeId,
            LocalDate date);
}