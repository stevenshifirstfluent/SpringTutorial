package sg.iss.nus.spring.tutorial.interceptor.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    // Not intercepted (for contrast)
    @GetMapping("/user/view")
    public String viewProfile()
    { 
    		return "viewProfile()"; 
    	}

    @GetMapping("/user/update-email")
    public String updateEmail() 
    { 
    		return "updateEmail()"; 
    	}

    @GetMapping("/user/update-notification")
    public String updateNotification() 
    { 
    		return "updateNotification()"; 
    	}
}
