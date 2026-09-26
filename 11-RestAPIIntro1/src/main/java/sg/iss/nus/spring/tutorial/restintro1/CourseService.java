package sg.iss.nus.spring.tutorial.restintro1;

import java.util.List;
import java.util.Optional;

public interface CourseService {

	List<Course> findAllCourses();
	
	Optional<Course> findCourse(int id);  // Use Optional to handle cases where the course might not be found
    
	Course createCourse(Course course);   // Return the created course
    
	void deleteCourse(int id);            // Delete a course by its ID
	
	Course updateCourse(int id, Course course); // Update an existing course by its ID
}
