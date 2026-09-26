package sg.iss.nus.spring.tutorial.restintro1;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer>{

	
}
