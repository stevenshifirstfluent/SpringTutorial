package sg.iss.nus.spring.tutorial.springcontroller.demo3;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(Model model) {
        // Add data to the model
        model.addAttribute("message", "Hello, World!");
        return "greetingPage"; // Resolves to src/main/resources/templates/greetingPage.html
    }
}