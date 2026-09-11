package sg.iss.nus.spring.tutorial.jpa.demo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDataDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataDemoApplication.class, args);
	}
	
	@Bean
    CommandLineRunner init(CitizenRepository citizens) {
        return args -> {
            // Build entities
            BirthCertificate bc = new BirthCertificate();
            bc.setRegistrationNo("REG-001");
            bc.setName("Alice");
            bc.setDeclaredBy("Father");

            Citizen c = new Citizen();
            c.setProfession("Engineer");
            c.setBirthCert(bc); // cascade=ALL will persist bc together

            // Save via repository (owner side)
            citizens.save(c);

            System.out.println("   Data inserted via Spring Data JPA repository.");
            System.out.println("   Check logs for DDL showing snake_case:");
            System.out.println("   - Table: birth_certificate, citizen");
            System.out.println("   - Columns: registration_no, declared_by, birth_cert_id\n");
        };
    }

}
