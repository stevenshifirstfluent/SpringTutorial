package sg.iss.nus.spring.tutorial;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

@Entity
public class Student {

	@Id
	private int id;
	@Column(name = "matric_no")
	private String matricNo;
	private String name;
	private double cap;
	@ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    private Department department;
	
	@ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> enrolledCourses;
	
	public Student() {
		// TODO Auto-generated constructor stub
	}
	
	public Student(int id, String name, String matricNo, double cap) {
		this.id = id;
		this.name = name;
		this.matricNo = matricNo;
		this.cap = cap;
	}
	

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getMatricNo() {
		return matricNo;
	}



	public void setMatricNo(String matricNo) {
		this.matricNo = matricNo;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public double getCap() {
		return cap;
	}



	public void setCap(double cap) {
		this.cap = cap;
	}
	
	

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@Override
	public String toString() {
		return "Student [id=" + id + ", matricNo=" + matricNo + ", name=" + name + ", cap=" + cap + "]";
	}

	

	
}
