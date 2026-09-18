package sg.iss.nus.spring.tutorial.sessiondemo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/protected")
public class ProtectedController {

  @GetMapping("/list-users")
  public String listUsers(HttpSession session, Model model) {
    String username = (String) session.getAttribute("username");
    if (username == null) {
      // user not logged in → redirect to /common/login
      return "redirect:/common/login";
    }

    model.addAttribute("username", username);
    return "list-users"; // resolved to /templates/list-users.html
  }
  
  @GetMapping("/logout")
  public String logout(HttpSession sessionObj) {
    sessionObj.removeAttribute("username");
    
    //sessionObj.invalidate();
    
    return "redirect:/common/login";
  }

}
