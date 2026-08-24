package sg.iss.nus.spring.jpqltutorial.aggregates;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    @Query("SELECT d.name, COUNT(s), AVG(s.cap) FROM Department d JOIN d.students s GROUP BY d.name")
    public List<Object[]> aggregateQuery();
    
    @Query("SELECT d.name, COUNT(s), AVG(s.cap) FROM Department d JOIN d.students s GROUP BY d HAVING COUNT(s)>=5")
    public List<Object[]> aggregateQueryWithFilter();
}
