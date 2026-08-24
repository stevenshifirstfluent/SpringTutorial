package sg.iss.nus.spring.tutorial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class JpqlTutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpqlTutorialApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(StudentRepository studentRepo) {
		 return args -> {
			 	List<Student> students = new ArrayList();
			 // Populate the list with Student objects
	            students.add(new Student(1, "Batman", "A00001A", 4.1));
	            students.add(new Student(2, "Superman", "A00002B", 3.5));
	            students.add(new Student(3, "Wonder Woman", "A00003C", 2.5));
	            students.add(new Student(4, "Flash", "A00004D", 2.1));
	            students.add(new Student(5, "Green Lantern", "A00005E", 3.7));
	            students.add(new Student(6, "Iron Man", "A00006F", 4.7));
	            students.add(new Student(7, "Captain America", "A00007G", 2.6));
	            students.add(new Student(8, "Black Widow", "A00008H", 3.6));
	            // Pre-populate student data
	            studentRepo.saveAll(students);
	            
	            System.out.println("Query all the students...");
	            // Perform JPQL query and print results
//	            List<Student> jpqlStudents = studentRepo.findAllStudents();
//	            jpqlStudents.forEach(student -> System.out.println(student));
	            
//	            System.out.println("Query all the students order by name...");
//	            List<Student> jpqlOrderedStudents = studentRepo.findAllStudentsWithOrder();
//	            jpqlOrderedStudents.forEach(student -> System.out.println(student));
//	            
//	            System.out.println("Query all the students by matching matricNo with ending character...");
//	            List<Student> jpqlMatricNoEndingMatchedStudents = studentRepo.findStudentsByMatricNo();
//	            jpqlMatricNoEndingMatchedStudents.forEach(student -> System.out.println(student));
	            
	            System.out.println("Query student by matching matricNo...");
	            Student jpqlMatricNoMatchedStudent = studentRepo.findStudentByMatricNo("A00001A");
	            System.out.println(jpqlMatricNoMatchedStudent);
//	            
//	            //Quiz
//	            System.out.println("Quiz: Query student by sorting matricNo...");
//	            List<Student> jpqlOrderedMatricNoStudents = studentRepo.findAllStudentsOrderByMatricNo();
//	            jpqlOrderedMatricNoStudents.forEach(student -> System.out.println(student));
//	            
//	            System.out.println("Quiz: Query student by matching name starts with 'B'...");
//	            List<Student> jpqlNameStartsBStudents = studentRepo.findAllStudentsMatchingNameStartsB();
//	            jpqlNameStartsBStudents.forEach(student -> System.out.println(student));
//	            
//	            System.out.println("Quiz: Query student by matching name starts with given text...");
//	            List<Student> jpqlNameStartsWithStudents = studentRepo.findAllStudentsMatchingNameStartsWith("B");
//	            jpqlNameStartsWithStudents.forEach(student -> System.out.println(student));
	        };
	}
	
}
