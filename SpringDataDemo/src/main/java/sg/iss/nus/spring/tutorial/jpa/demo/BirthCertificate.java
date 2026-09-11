package sg.iss.nus.spring.tutorial.jpa.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class BirthCertificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String registrationNo;

    private String name;

    private String declaredBy;

    // inverse side (no join column here)
    @OneToOne(mappedBy = "birthCert")
    private Citizen citizen;

    // getters & setters
    public Long getId() { return id; }
    public String getRegistrationNo() { return registrationNo; }
    public void setRegistrationNo(String registrationNo) { this.registrationNo = registrationNo; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDeclaredBy() { return declaredBy; }
    public void setDeclaredBy(String declaredBy) { this.declaredBy = declaredBy; }
    public Citizen getCitizen() { return citizen; }
    public void setCitizen(Citizen citizen) { this.citizen = citizen; }
}