package sg.iss.nus.spring.tutorial.data.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	  // JPA query method required by the slide: SearchByName
	  // Using "ContainingIgnoreCase" so partial and case-insensitive matches work
	  List<Customer> findByNameContainingIgnoreCase(String name);
	}
