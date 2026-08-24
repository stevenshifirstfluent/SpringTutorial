package sg.iss.nus.spring.jpqltutorial.aggregates;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpqlTutorialApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpqlTutorialApplication.class, args);
	}
	
	static Student s(String matricNo, String name, double cap, Department dept, List<Course> courses) {
	    Student st = new Student();
	    st.setMatricNo(matricNo);
	    st.setName(name);
	    st.setCap(cap);
	    st.setDepartment(dept);        
	    st.setCourses(courses);        // assign multiple courses
	    return st;
	}

	@Bean
    CommandLineRunner commandLineRunner(DepartmentService departmentService, StudentRepository studentRepo, DepartmentRepository departmentRepo, CourseRepository courseRepo) {
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
            List<Student> students = List.of(
            	    s("A00001A", "Batman", 4.1, dc, List.of(course1)),
            	    s("A00002B", "Superman", 3.5, dc, List.of(course1)),
            	    s("A00003C", "Wonder Woman", 2.5, dc, List.of(course1)), // only one course
            	    s("A00004D", "Flash", 2.1, dc, List.of(course2)),        // different course
            	    s("A00005E", "Green Lantern", 3.7, dc, List.of(course1)),
            	    s("A00006F", "Iron Man", 4.7, marvel, List.of(course1, course2)),
            	    s("A00007G", "Captain America", 2.6, marvel, List.of(course1)),
            	    s("A00008H", "Black Widow", 3.6, marvel, List.of(course2)),
            	    s("A00009I", "Hulk", 3.0, marvel, List.of(course1))
            	);

            studentRepo.saveAll(students);

         // Perform JPQL query and print results
//            List<Object[]> results = departmentService.aggregateQuery();
//            for (Object[] result : results) {
//                System.out.println("Department: " + result[0] + ", Student Count: " + result[1] + ", Average CAP: " + result[2]);
//            }
            
            List<Object[]> filteredResults = departmentService.aggregateQueryWithFilter();
            System.out.print("Department student count is equal or more than 5: ");
            for (Object[] result : filteredResults) {
                System.out.println("Department: " + result[0] + ", Student Count: " + result[1] + ", Average CAP: " + result[2]);
            }
        };
    }
}
