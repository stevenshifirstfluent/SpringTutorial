package sg.iss.nus.spring.tutorial.jpa.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="students")
public class Student {

	@Id
	private int id;
	
	@Column(name="matric_number")
	private int matricNo;
	
	private String name;
	public Student() {
		// TODO Auto-generated constructor stub
	}
}