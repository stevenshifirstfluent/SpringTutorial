package sg.iss.nus.spring.tutorial.jpa.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="heads")
public class Head {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="h_id")
	private int id;
	
	@Column(name="h_name")
	private String name;
	
	@OneToOne(mappedBy = "myHead")
	private Department myDepartment;
	
	public Head() {
		// TODO Auto-generated constructor stub
	}

}
