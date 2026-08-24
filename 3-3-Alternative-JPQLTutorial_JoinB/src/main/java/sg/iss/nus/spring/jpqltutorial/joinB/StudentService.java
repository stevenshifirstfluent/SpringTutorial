package sg.iss.nus.spring.jpqltutorial.joinB;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class StudentService {

	@Autowired
    private StudentRepository studentRepository;

    @Transactional
    public List<Student> findStudentsByCourseCode(String courseCode) {
        return studentRepository.findStudentsByCourseCode(courseCode);
    }

}
