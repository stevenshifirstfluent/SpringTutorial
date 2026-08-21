package sg.iss.nus.spring.tutorial.jpa.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="Calls")
public class Call {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private long duration;
	@ManyToOne
	private MobilePhone mobilePhone;
	public Call() {
		// TODO Auto-generated constructor stub
	}

}
