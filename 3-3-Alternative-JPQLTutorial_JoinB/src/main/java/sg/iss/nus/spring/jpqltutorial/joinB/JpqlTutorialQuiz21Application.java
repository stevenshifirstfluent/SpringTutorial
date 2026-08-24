package sg.iss.nus.spring.jpqltutorial.joinB;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpqlTutorialQuiz21Application {

	public static void main(String[] args) {
		SpringApplication.run(JpqlTutorialQuiz21Application.class, args);
	}

	@Bean
    CommandLineRunner commandLineRunner(StudentService studentService, StudentRepository studentRepo, DepartmentRepository departmentRepo, CourseRepository courseRepo) {
        return args -> {
            // Save departments
            Department dc = new Department();
            dc.setName("DC");
            Department marvel = new Department();
            marvel.setName("Marvel");
            departmentRepo.saveAll(Arrays.asList(dc, marvel));

            // Save courses
            Course course1 = new Course();
            course1.setCode("C001");
            course1.setDescription("Course 1 Description");
            Course course2 = new Course();
            course2.setCode("C002");
            course2.setDescription("Course 2 Description");
            courseRepo.saveAll(Arrays.asList(course1, course2));

            // Save students
            Student student1 = new Student();
            student1.setMatricNo("A00001A");
            student1.setName("Batman");
            student1.setCap(4.1);
            student1.setDepartment(dc);
            student1.setCourses(Arrays.asList(course1, course2));

            Student student2 = new Student();
            student2.setMatricNo("A00002B");
            student2.setName("Superman");
            student2.setCap(3.5);
            student2.setDepartment(dc);
            student2.setCourses(Arrays.asList(course1));

            studentRepo.saveAll(Arrays.asList(student1, student2));

            // Perform JPQL query and print results
            List<Student> students = studentService.findStudentsByCourseCode("C002");
            students.forEach(student -> System.out.println(student.getName() + ": " + student.getMatricNo()));
        };
    }
}
