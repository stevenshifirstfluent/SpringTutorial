package sg.iss.nus.spring.tutorial;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Integer>{

	@Query("select d from Department d")
	public List<Department> findAllDepartments();
}
