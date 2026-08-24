package sg.iss.nus.spring.tutorial;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Lecturer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String name;
	
	@OneToMany(mappedBy = "lecturer")
	private List<Course> courses;
	
	public Lecturer() {
		// TODO Auto-generated constructor stub
	}

}
