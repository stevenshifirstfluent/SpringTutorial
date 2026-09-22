package sg.iss.nus.spring.tutorial.interceptor.demo;

import java.time.LocalTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
public class TimeController {

    @GetMapping("/time/showtime")
    public String showTime(@RequestAttribute(name = "startTime", required = false) Long startTime,
                           Model model) throws InterruptedException {

        // simulate some work so execution time is visible
        Thread.sleep(1000);

        // Note: postHandle() will also add start/complete/execute to the model.
        // Adding startTime here only to demonstrate @RequestAttribute use:
        if (startTime != null) {
            model.addAttribute("startTime", startTime);
        }
        return "showtime"; // Thymeleaf template name
    }
}