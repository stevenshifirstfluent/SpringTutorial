package sg.iss.nus.spring.tutorial.springcontrollerdemo2;

import java.util.Random;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController2 {

	private final Customer customer;
	public CustomerController2() {
		Random random = new Random();
        // Generate a random ID for demonstration purposes (you might use a more robust ID generation strategy)
        String randomId = String.valueOf(random.nextInt(10000)); // Generates a random ID between 0 and 9999
        this.customer = new Customer(randomId, "Harrison", "65164831");
	}
	
	@GetMapping("/welcome")
    public String welcome(Model model) {
        model.addAttribute("message", "Welcome to the Web Application Development class!");
        return "welcome"; // This should correspond to a Thymeleaf template named 'welcome.html'
    }

    @GetMapping("/customer2")
    public String customerInfo(Model model) {
        model.addAttribute("customer", this.customer);
        return "customer"; // This should correspond to a Thymeleaf template named 'customer.html'
    }

}
