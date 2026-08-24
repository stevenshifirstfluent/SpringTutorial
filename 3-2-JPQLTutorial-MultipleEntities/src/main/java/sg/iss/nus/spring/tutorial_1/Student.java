package sg.iss.nus.spring.tutorial_1;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String name;
	
	@Column(name = "matric_no")
	private String matricNo;
	
	private double cap;
	
	@ManyToOne
    @JoinColumn(name = "department_id", referencedColumnName = "id")
	private Department department;
	
	public Student() {
		// TODO Auto-generated constructor stub
	}
	
	public Student(int id, String name, String matricNo, double cap, Department department) {
        this.id = id;
        this.name = name;
        this.matricNo = matricNo;
        this.cap = cap;
        this.department = department;
    }

	// Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatricNo() {
        return matricNo;
    }

    public void setMatricNo(String matricNo) {
        this.matricNo = matricNo;
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
        return "Student [id=" + id + ", name=" + name + ", matricNo=" + matricNo + ", cap=" + cap + ", department=" + department.getName() + "]";
    }
}
