package sg.iss.nus.spring.tutorial.restintro1;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional(readOnly=true)
public class CourseServiceImpl implements CourseService {
	@Resource
	private CourseRepository courseRepository;
	@Override
	public List<Course> findAllCourses() {
		return courseRepository.findAll();
	}
	
	@Override
    public Optional<Course> findCourse(int id) {
        return courseRepository.findById(id); // Use findById to get an Optional<Course>
    }

    @Override
    @Transactional(readOnly = false)
    public Course createCourse(Course course) {
        return courseRepository.save(course); // Use save to create a new course
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteCourse(int id) {
        courseRepository.deleteById(id); // Use deleteById to remove a course
    }
    
    @Override
    @Transactional(readOnly = false)
    public Course updateCourse(int id, Course course) {
        Optional<Course> existingCourseOpt = courseRepository.findById(id);

        if (existingCourseOpt.isPresent()) {
            Course existingCourse = existingCourseOpt.get();
            existingCourse.setCode(course.getCode());
            existingCourse.setName(course.getName());
            existingCourse.setDescription(course.getDescription());
            try {
            	return courseRepository.save(existingCourse); // Save the updated course
            }catch(Exception e) {
            	return null;
            }
            
        } else {
        	throw new EntityNotFoundException("Course not found with id: " + id);
        }
    }

}
