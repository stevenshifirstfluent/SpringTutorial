package sg.iss.nus.spring.tutorial.restintro1;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api")
public class CourseAPIController {

	@Autowired
	private CourseService courseService;
	
	@GetMapping("/courses")
	public List<Course> getAllCourses(){
		return courseService.findAllCourses();
	}
	
	@GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable("id") int id) {
        Optional<Course> optCourse = courseService.findCourse(id);
        
        if(optCourse.isPresent()) {
        	return new ResponseEntity<Course>(optCourse.get(), HttpStatus.OK);
        }
        else {
        	return new ResponseEntity<Course>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course inCourse) {
	    	try {
	    		Course retCourse = courseService.createCourse(inCourse);
	    		return new ResponseEntity<Course>(retCourse, HttpStatus.CREATED);
	    	}catch(Exception e) {
	    		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	    	}
    }
    
    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> editCourse(@PathVariable("id") int id, @RequestBody Course inCourse){
	    	try {
	    		Course course = courseService.updateCourse(id, inCourse);
	    		return new ResponseEntity<Course>(course, HttpStatus.OK);
	    	}catch(Exception e) {
	    		if(e instanceof EntityNotFoundException) {
	    			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    		}
	    		return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
	    	}
    	
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<HttpStatus> deleteCourse(@PathVariable("id") int id) {
	    	try {
	    		courseService.deleteCourse(id);
	    		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	    	}catch(Exception e) {
	    		return new ResponseEntity<HttpStatus>(HttpStatus.EXPECTATION_FAILED);
	    	}
        
    }

}
