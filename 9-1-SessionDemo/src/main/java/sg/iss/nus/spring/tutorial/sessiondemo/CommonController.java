package sg.iss.nus.spring.tutorial.sessiondemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/common")
public class CommonController {

  @GetMapping("/login")
  public String loginForm() {
    return "login"; // /templates/login.html
  }
  
  @GetMapping("/demolistuser")
  public String demoListUser()
  {
	  return "list-users";
  }

  @PostMapping("/login")
  public String doLogin(User user, HttpSession session) {
    session.setAttribute("username", user.getUsername());
    // after login, redirect into protected area
    return "redirect:/protected/list-users";
  }

}
