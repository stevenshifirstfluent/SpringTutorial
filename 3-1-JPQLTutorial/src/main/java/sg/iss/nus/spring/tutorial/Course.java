package sg.iss.nus.spring.tutorial;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String code;
	private String description;
	
	@ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;

	@ManyToMany(mappedBy = "enrolledCourses")
    private List<Student> students;
	
	public Course() {
		// TODO Auto-generated constructor stub
	}

}
