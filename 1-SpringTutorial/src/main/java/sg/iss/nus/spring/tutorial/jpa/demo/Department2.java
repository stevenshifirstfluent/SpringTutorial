package sg.iss.nus.spring.tutorial.jpa.demo;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="departments")
public class Department2 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="d_id")
	private int id;
	
	@Column(name="d_name")
	private String name;
	
	@OneToMany(mappedBy = "myDepartment")
	private List<Staff> myStaffs;
	public Department2() {
		// TODO Auto-generated constructor stub
	}

}
