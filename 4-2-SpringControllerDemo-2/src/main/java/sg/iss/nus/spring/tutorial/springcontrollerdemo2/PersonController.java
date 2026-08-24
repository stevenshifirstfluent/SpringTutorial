package sg.iss.nus.spring.tutorial.springcontrollerdemo2;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PersonController {

	public PersonController() {
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("/hello")
	public String hello(Model model) {
		Person person = new Person();
		person.setFirstName("Harrison");
		person.setLastName("Lawrenc");
		model.addAttribute("person1", person);
		return "display-person";
	}

}
