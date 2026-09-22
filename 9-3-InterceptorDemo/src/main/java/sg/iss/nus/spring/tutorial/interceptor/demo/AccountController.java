package sg.iss.nus.spring.tutorial.interceptor.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
	
	//http://localhost:8081/account/balance?page=2&sort=date 
    @GetMapping("/account/balance")
    public String viewBalance(@RequestParam(name="page", defaultValue = "1") int page,
                              @RequestParam(name="sort", defaultValue = "date") String sort) throws InterruptedException {
        // Simulate work
        Thread.sleep(120);
        return "viewBalance(page=" + page + ", sort=" + sort + ")";
    }

    @GetMapping("/account/withdraw")
    public String withdraw() { return "withdraw()"; }

    @GetMapping("/account/transfer")
    public String transfer() { return "transfer()"; }
}
