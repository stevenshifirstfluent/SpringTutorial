package sg.iss.nus.spring.jpqltutorial.quiz21;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.matricNo = :matricNo")
    public List<Course> findCoursesByStudentMatricNo(@Param("matricNo") String matricNo);
}
