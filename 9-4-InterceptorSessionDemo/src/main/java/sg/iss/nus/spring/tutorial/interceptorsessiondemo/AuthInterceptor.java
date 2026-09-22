package sg.iss.nus.spring.tutorial.interceptorsessiondemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthInterceptor implements HandlerInterceptor{

	public AuthInterceptor() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login"); // Redirect to login if no user is found in session
            return false;
        }
        return true;
	}
}
