package sg.iss.nus.spring.tutorial.sessiondemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
public class SessionController {

	public SessionController() {
		// TODO Auto-generated constructor stub
	}
	
	@GetMapping("/count")
    public String count(HttpSession session) {
        Integer count = (Integer) session.getAttribute("count");
        if (count == null) {
            count = 0;
        }
        session.setAttribute("count", ++count);
        return "Session count is " + count;
    }
	
	@GetMapping("/servletCount")
	public String servletCount(HttpServletRequest request)
	{
		HttpSession sessionObj = request.getSession();
		Integer count = (Integer) sessionObj.getAttribute("count");
        if (count == null) {
            count = 0;
        }
        sessionObj.setAttribute("count", ++count);
        return "Session servlet count is " + count;
	}
	
	@RequestMapping("/removeCount")
	public String removeCount(HttpSession sessionObj)
	{
		sessionObj.removeAttribute("count");
		return "Session count is cleared";
	}
}
