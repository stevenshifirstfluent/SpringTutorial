package sg.iss.nus.spring.tutorial.springcontrollerdemo2;

import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {

	public CustomerController() {
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("/")
	public String welcome(Model model) {
		model.addAttribute("message", "Welcome to the Web Application Development class!");
        return "welcome"; // This should correspond to a Thymeleaf template named 'welcome.html'
	}

	@GetMapping("/customer")
    public String customerInfo(Model model) {
		Random random = new Random();
		String randomId = String.valueOf(random.nextInt(10000));
        //Customer customer = new Customer("135", "Harrison", "65164831");
		Customer customer = new Customer(randomId, "Harrison", "65164831");
        model.addAttribute("customer", customer);
        return "customer"; // This should correspond to a Thymeleaf template named 'customer.html'
    }
}
