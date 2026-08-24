package sg.iss.nus.spring.tutorial_1;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpqlTutorial1Application {

	public static void main(String[] args) {
		SpringApplication.run(JpqlTutorial1Application.class, args);
	}

	@Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepo, DepartmentRepository departmentRepo) {
        return args -> {
            // Save departments
            Department dc = new Department(1, "DC");
            Department marvel = new Department(2, "Marvel");
            departmentRepo.saveAll(Arrays.asList(dc, marvel));

            // Save students
            studentRepo.saveAll(Arrays.asList(
                    new Student(1, "Batman", "A00001A", 4.1, dc),
                    new Student(2, "Superman", "A00002B", 3.5, dc),
                    new Student(3, "Wonder Woman", "A00003C", 2.5, dc),
                    new Student(4, "Flash", "A00004D", 2.1, dc),
                    new Student(5, "Green Lantern", "A00005E", 3.7, dc),
                    new Student(6, "Iron Man", "A00006F", 4.7, marvel),
                    new Student(7, "Captain America", "A00007G", 2.6, marvel),
                    new Student(8, "Black Widow", "A00008H", 3.6, marvel),
                    new Student(9, "Hulk", "A00009I", 3.0, marvel)
            ));

            // Perform JPQL query and print results
            List<Student> students = studentRepo.findStudentsByDepartmentAndName("man", "DC");
            students.forEach(student -> System.out.println(student));
            
//            List<Student> students2 = studentRepo.findAllStudentsWithDepartments();
//            students2.forEach(student -> System.out.println("With same department: " + student));
        };
    }
}
