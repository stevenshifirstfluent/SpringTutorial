package sg.iss.nus.spring.tutorial_1;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	
	public Department() {
		// TODO Auto-generated constructor stub
	}


	public Department(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}

	
}
