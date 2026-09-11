package sg.iss.nus.spring.tutorial.jpa.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CitizenRepository extends JpaRepository<Citizen, Long> { }
