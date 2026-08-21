package sg.iss.nus.spring.tutorial.jpa.demo;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Lecturer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	
	@ManyToMany
	@JoinTable(name="lecturer_module", 
	joinColumns = @JoinColumn(name="lecturer_id"), 
	inverseJoinColumns = @JoinColumn(name="module_id"))
	private List<Module> teachings;
	public Lecturer() {
		// TODO Auto-generated constructor stub
	}

}
