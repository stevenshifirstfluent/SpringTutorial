package sg.iss.nus.spring.tutorial.springcontrollerdemo2;

public class Customer {

	private String id;
	public Customer(String id, String name, String telephoneNumber) {
		super();
		this.id = id;
		this.name = name;
		this.telephoneNumber = telephoneNumber;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	private String name;
	private String telephoneNumber;
	
	public Customer() {
		// TODO Auto-generated constructor stub
	}
	
	
	
	
}
