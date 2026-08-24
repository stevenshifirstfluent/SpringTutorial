package sg.iss.nus.spring.jpqltutorial.joinB;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Integer>{

	@Query("SELECT s FROM Student s JOIN s.courses c WHERE c.code = :courseCode")
    public List<Student> findStudentsByCourseCode(@Param("courseCode") String courseCode);
}
