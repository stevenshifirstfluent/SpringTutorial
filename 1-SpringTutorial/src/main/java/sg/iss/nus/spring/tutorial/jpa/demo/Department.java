package sg.iss.nus.spring.tutorial.jpa.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="departments")
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="d_id")
	private int id;
	
	@Column(name="d_name")
	private String name;
	
	@OneToOne
	@JoinColumn(name="head_id")
	public Head myHead;
	public Department() {
		// TODO Auto-generated constructor stub
	}

}
