package sg.iss.nus.spring.tutorial_1;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Integer>{

	@Query("SELECT s FROM Student s WHERE s.name LIKE %:name% AND s.department.name = :departmentName")
    List<Student> findStudentsByDepartmentAndName(@Param("name") String name, @Param("departmentName") String departmentName);

	@Query("SELECT s FROM Student s JOIN s.department d")
    List<Student> findAllStudentsWithDepartments();
}
