package sg.iss.nus.spring.tutorial.interceptor.demo;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "Interceptor demo!";
    }
    
}
