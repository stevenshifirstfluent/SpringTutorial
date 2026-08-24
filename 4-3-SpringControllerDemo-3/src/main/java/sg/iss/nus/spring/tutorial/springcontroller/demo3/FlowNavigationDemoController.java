package sg.iss.nus.spring.tutorial.springcontroller.demo3;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FlowNavigationDemoController {

    // 1. Return a View Name (renders Thymeleaf template directly)
    @GetMapping("/hello")
    public String helloPage(Model model) {
        model.addAttribute("message", "Hello from Controller!");
        return "helloView"; // resolves to src/main/resources/templates/helloView.html
    }

    // 2. Redirect to another URL
    @PostMapping("/submit")
    public String handleFormSubmission(@RequestParam String name) {
        // do something with 'name' (e.g., save to DB)
        // redirect prevents form resubmission
        return "redirect:/thank-you";
    }

    // 3. Forward to another handler (internal, no browser redirect)
    @GetMapping("/forward-example")
    public String forwardExample() {
        // internally forwards request to /hello
        return "forward:/hello";
    }

    // Thank-you page after redirect
    @GetMapping("/thank-you")
    public String thankYouPage(Model model) {
        model.addAttribute("message", "Thanks for submitting the form!");
        return "thankYou"; // resolves to thankYou.html
    }
    
    @GetMapping("/start-forward")
    public String startForward(Model model) {
        // Add data to be forwarded
        model.addAttribute("username", "Batman");
        model.addAttribute("role", "Hero");

        // Forward internally to /forward-target
        return "forward:/forward-target";
    }

    @GetMapping("/forward-target")
    public String forwardTarget(@RequestParam(required = false) String name,
                                Model model) {
        // Model attributes from the forward are preserved
        // You can also add more here if needed
        model.addAttribute("extra", "This is extra data added at target");
        return "forwardResult"; // Thymeleaf page
    }
}