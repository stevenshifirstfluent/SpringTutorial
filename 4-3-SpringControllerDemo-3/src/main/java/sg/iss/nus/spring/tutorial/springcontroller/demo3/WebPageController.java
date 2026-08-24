package sg.iss.nus.spring.tutorial.springcontroller.demo3;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {
  @GetMapping("/searchGet")  
  public String getPage()  { return "searchGet";  }
  
  @GetMapping("/searchPost") 
  public String postPage() { return "searchPost"; }
}
