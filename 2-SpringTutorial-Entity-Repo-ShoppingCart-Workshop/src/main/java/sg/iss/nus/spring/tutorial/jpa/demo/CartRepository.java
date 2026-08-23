package sg.iss.nus.spring.tutorial.jpa.demo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
