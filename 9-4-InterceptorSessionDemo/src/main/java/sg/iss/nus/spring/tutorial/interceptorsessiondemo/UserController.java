package sg.iss.nus.spring.tutorial.interceptorsessiondemo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

	public UserController() {
		// TODO Auto-generated constructor stub
	}
	
	@PostMapping("/login")
    public ModelAndView login(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              HttpSession session) {
        if ("user".equals(username) && "pass".equals(password)) {
            session.setAttribute("user", username);
            return new ModelAndView("redirect:/dashboard");
        }
        return new ModelAndView("login", "error", "Invalid username or password.");
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        return "dashboard";
    }
    
    @GetMapping("/showtime")
    public String showTime(Model model) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        model.addAttribute("currentTime", formattedDateTime);
        return "showtime";
    }

    @GetMapping("/public")
    public String publicPage() {
        return "public";
    }

}
