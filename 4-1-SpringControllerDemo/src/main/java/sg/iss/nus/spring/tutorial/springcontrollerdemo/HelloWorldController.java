package sg.iss.nus.spring.tutorial.springcontrollerdemo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

	public HelloWorldController() {
		// TODO Auto-generated constructor stub
	}

	@GetMapping("/hello")
	public String hello()
	{
		System.out.println("in the method hello()");
		return "Hello World! This is a welcome page";
	}
}
