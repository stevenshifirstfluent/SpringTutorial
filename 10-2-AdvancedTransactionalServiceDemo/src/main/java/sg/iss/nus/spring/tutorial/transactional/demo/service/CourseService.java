package sg.iss.nus.spring.tutorial.transactional.demo.service;

import java.util.List;

import sg.iss.nus.spring.tutorial.transactional.demo.model.Course;

public interface CourseService {

    List<Course> findAllCourses();

    Course createCourse(Course course);
}