package sg.iss.nus.spring.tutorial.interceptor.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonController {
    @GetMapping("/common/login")
    public String login() 
    { 
    	    return "login()"; 
    	    
    }

    @GetMapping("/common/logout")
    public String logout() { return "logout()"; }

    @GetMapping("/common/about")
    public String about() { return "about()"; }
}