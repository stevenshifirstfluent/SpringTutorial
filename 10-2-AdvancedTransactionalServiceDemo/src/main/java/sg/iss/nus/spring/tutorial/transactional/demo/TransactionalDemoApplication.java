package sg.iss.nus.spring.tutorial.transactional.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import sg.iss.nus.spring.tutorial.transactional.demo.model.Course;
import sg.iss.nus.spring.tutorial.transactional.demo.repository.CourseRepository;

@SpringBootApplication
public class TransactionalDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionalDemoApplication.class, args);
	}
}
