package sg.iss.nus.spring.tutorial.restintro1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("courses")
public class CourseController {

	@Autowired
	private CourseService courseService;
	
	@GetMapping(value = "/list")
	public String listCourses(Model model) {
		model.addAttribute(
		"courses", courseService.findAllCourses());
		return "courses";
	}
}
