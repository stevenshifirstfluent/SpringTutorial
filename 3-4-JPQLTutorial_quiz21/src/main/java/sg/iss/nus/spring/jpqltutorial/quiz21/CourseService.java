package sg.iss.nus.spring.jpqltutorial.quiz21;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public List<Course> findCoursesByStudentMatricNo(String matricNo) {
        return courseRepository.findCoursesByStudentMatricNo(matricNo);
    }
}
