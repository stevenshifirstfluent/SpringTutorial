package sg.iss.nus.spring.tutorial.jpa.demo;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
public class MobilePhone {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String number;
	@OneToMany(mappedBy = "mobilePhone")
	private List<Call> calls;
	public MobilePhone() {
		// TODO Auto-generated constructor stub
	}

}
