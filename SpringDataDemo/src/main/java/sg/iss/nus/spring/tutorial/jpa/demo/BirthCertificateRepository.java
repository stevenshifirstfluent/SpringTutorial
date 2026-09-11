package sg.iss.nus.spring.tutorial.jpa.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BirthCertificateRepository extends JpaRepository<BirthCertificate, Long> { }
