package sg.iss.nus.spring.jpqltutorial.quiz21;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer>{

	
}
