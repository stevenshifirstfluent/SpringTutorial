package sg.iss.nus.spring.tutorial.jpa.demo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Module {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String code;
	private String name;
	
	@ManyToMany(mappedBy = "teachings")
	private List<Lecturer> lecturers;
	
	public Module() {
		// TODO Auto-generated constructor stub
	}

}
