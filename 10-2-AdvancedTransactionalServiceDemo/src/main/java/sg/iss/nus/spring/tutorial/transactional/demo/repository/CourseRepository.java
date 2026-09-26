package sg.iss.nus.spring.tutorial.transactional.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sg.iss.nus.spring.tutorial.transactional.demo.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
