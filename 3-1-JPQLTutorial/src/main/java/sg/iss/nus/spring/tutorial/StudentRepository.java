package sg.iss.nus.spring.tutorial;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudentRepository extends JpaRepository<Student, Integer>{

	@Query("SELECT s FROM Student s")
	public List<Student> findAllStudents();
	
	@Query("SELECT s FROM Student s ORDER BY s.name")
	public List<Student> findAllStudentsWithOrder();
	
	@Query("SELECT s FROM Student s WHERE s.matricNo LIKE '%C' OR s.matricNo LIKE '%E'")
	public List<Student> findStudentsByMatricNo();
	
	@Query("SELECT s FROM Student s WHERE s.matricNo = :matricNo")
	public Student findStudentByMatricNo(@Param("matricNo") String matricNo);
	
	
	
	//Quiz
	@Query("SELECT s FROM Student s ORDER BY s.matricNo")
	public List<Student> findAllStudentsOrderByMatricNo();
	
	@Query("SELECT s FROM Student s WHERE s.name LIKE 'B%'")
	public List<Student> findAllStudentsMatchingNameStartsB();
	
	@Query("SELECT s FROM Student s WHERE s.name LIKE CONCAT(:namePrefix, '%')")
	public List<Student> findAllStudentsMatchingNameStartsWith(@Param("namePrefix") String startsWithText);

	//End Quiz
	
}
