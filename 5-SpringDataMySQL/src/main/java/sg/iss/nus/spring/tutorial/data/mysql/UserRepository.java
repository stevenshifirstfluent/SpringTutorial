package sg.iss.nus.spring.tutorial.data.mysql;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Additional query methods can be defined here
	@Query("SELECT u FROM User u WHERE u.username = :username")
	ArrayList<User> findByUsername(@Param("username") String username);
}
