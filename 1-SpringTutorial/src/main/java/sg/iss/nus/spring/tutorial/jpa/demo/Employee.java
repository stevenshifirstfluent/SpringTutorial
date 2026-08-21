package sg.iss.nus.spring.tutorial.jpa.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="employees")
public class Employee {

	@Id
	private int id;
	
	@Column(name="e_name", nullable=false)
	private String name;
	
	@Column(name="e_salary")
	private long salary;
	
	public Employee() {
		// TODO Auto-generated constructor stub
	}

}

