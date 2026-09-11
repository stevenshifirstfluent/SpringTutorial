package sg.iss.nus.spring.tutorial.jpa.demo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Citizen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // camelCase -> snake_case (profession stays profession)
    private String profession;

    // owning side: FK column will be birth_cert_id by default. We can set the customised name by define the name in the @JoinColumn annotation
    @OneToOne(optional = false, cascade = CascadeType.ALL)
    // If we want to use legacy spring boot 1.x version approach, the default database column name to be set as "birthCert_id" without customised name using @JoinColumn annotation
    // In this case, we can add below 2 settings into application.properties
    // spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
    // spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
    //@JoinColumn(name = "birthCert_id", nullable = false, unique = true)
    private BirthCertificate birthCert;

    // getters & setters
    public Long getId() { return id; }
    public String getProfession() { return profession; }
    public void setProfession(String profession) { this.profession = profession; }
    public BirthCertificate getBirthCert() { return birthCert; }
    public void setBirthCert(BirthCertificate birthCert) {
        this.birthCert = birthCert;
        if (birthCert.getCitizen() != this) birthCert.setCitizen(this);
    }
}