package sg.iss.nus.spring.tutorial.transactional.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

import sg.iss.nus.spring.tutorial.transactional.demo.model.Course;
import sg.iss.nus.spring.tutorial.transactional.demo.repository.CourseRepository;

@Service
@Transactional(readOnly = true)  // Read-only transactions by default
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Course> findAllCourses() {
        return courseRepository.findAll();
    }

    @Transactional(readOnly = false)  // Writing transactions (default is false)
    @Override
    public Course createCourse(Course course) {
    	// Log whether the transaction is read-only
        boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        System.out.println("Is transaction read-only? " + readOnly);
        return courseRepository.save(course);
    }
}