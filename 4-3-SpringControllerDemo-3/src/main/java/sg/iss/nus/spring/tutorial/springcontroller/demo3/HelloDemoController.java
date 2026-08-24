package sg.iss.nus.spring.tutorial.springcontroller.demo3;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloDemoController {

    @GetMapping("/hellodemo")
    public String handle(Model model) {
        // Format today’s date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        Calendar cal = Calendar.getInstance();

        // Add to model
        model.addAttribute("today", dateFormat.format(cal.getTime()));

        // Return Thymeleaf view name
        return "greetings";   // -> src/main/resources/templates/greetings.html
    }
}