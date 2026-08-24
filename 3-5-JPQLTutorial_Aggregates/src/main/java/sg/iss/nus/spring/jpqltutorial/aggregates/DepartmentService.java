package sg.iss.nus.spring.jpqltutorial.aggregates;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public List<Object[]> aggregateQuery() {
        return departmentRepository.aggregateQuery();
    }
    
    @Transactional
    public List<Object[]> aggregateQueryWithFilter(){
    	return departmentRepository.aggregateQueryWithFilter();
    }
}
