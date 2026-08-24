package sg.iss.nus.spring.tutorial.springcontrollerdemo2;

public class Person {

	public Person() {
		// TODO Auto-generated constructor stub
	}
	
	private String firstName;
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	private String lastName;
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
}
